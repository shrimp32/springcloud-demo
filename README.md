# Spring Cloud Demo 应用说明
关键点：

- 服务注册与发现
- 服务消费与负载均衡：Spring Cloud可以用RestTemplate+Ribbon和Feign来调用服务，实现服务的负载均衡
- 断路器
- 智能路由
- 配置中心
- 消息总线（Spring Cloud Bus）
- 服务链路追踪（Spring Cloud Sleuth）
- ​

##  服务注册中心

  ### 服务端配置eueka server:2000
   1. pom.xml
  ```xml
    <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-eureka-server</artifactId>
	</dependency>
  ```
   1. application.properties
   ```properties
    spring.application.name=eureka
    server.port=2000
    eureka.instance.hostname=localhost
    #server本身不作为客户端注册   
    eureka.client.register-with-eureka=false
    eureka.client.fetch-registry=false
    eureka.client.serviceUrl.defaultZone=http://localhost:${server.port}/eureka/
   ```
  ### 客户端配置 hello:2001示例应用
   1. pom.xml
   ```xml
     <dependency>
           <groupId>org.springframework.cloud</groupId>
           <artifactId>spring-cloud-starter-eureka</artifactId>
     </dependency>   
   ```
      1. application.properties
```properties
    #配置eureka服务端的访问地址
    eureka.client.serviceUrl.defaultZone=http://localhost:2000/eureka/
```

## cloud-config:3000 配置中心

在分布式系统中，由于服务数量巨多，为了方便服务配置文件统一管理，实时更新，所以需要分布式配置中心组件。分布式配置中心组件spring cloud config ，它支持配置服务放在配置服务的内存中（即本地），也支持放在远程Git仓库中。在spring cloud config 组件中，分两个角色，一是config server，二是config client。

### config server

1. pom.xml

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
```

2. 程序的入口Application类加上@EnableConfigServer注解开启配置服务器的功能
3. 在程序的配置文件application.properties文件配置以下

```properties
# 配置git仓库地址
spring.cloud.config.server.git.uri=https://github.com/shrimp32/springcloud-demo
# 配置仓库路径
spring.cloud.config.server.git.searchPaths=./
# 配置仓库的分支
spring.cloud.config.label=master
# 访问git仓库的用户名和密码，如果Git仓库为公开仓库，可以不填写用户名和密码，如果是私有仓库需要填写
spring.cloud.config.server.git.username=your username
spring.cloud.config.server.git.password=your password
```

4. 在远程仓库上创建hello应用的配置文件，hello-dev.properties如下：

```
app.name=hello
app.version=1.0

spring.application.name=hello
eureka.client.serviceUrl.defaultZone=http://localhost:2000/eureka/
```

- 配置文件的映射规则
  - 文件名：{application}-{profile}.properties
  - /{application}/{profile}[/{label}]
    /{application}-{profile}.yml
     /{label}/{application}-{profile}.yml
     /{application}-{profile}.properties
     /{label}/{application}-{profile}.properties
  - {label}对应git上的分支名

### config client

将hello改造为config client。

1. pom.xml中加入

   ```xml
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-config</artifactId>
           </dependency>
   ```

2. 在程序的配置文件bootstrap.properties文件配置以下

```properties
#访问配置服务器的配置
#spring.cloud.config.label 指明远程仓库的分支
spring.cloud.config.label=master
spring.cloud.config.profile=dev
spring.cloud.config.uri= http://localhost:3000/
```

3. HelloController写一个API接口“／hi”，返回从配置中心读取的app.name变量的值，代码如下：

```java
    @Value("${app.name}")
    String name;
    @RequestMapping(value = "/hi")
    public String hi(){
        String r = "Hello, " + name +"! port: " + port;
        return r;
    }
```

4. 访问http://localhost:2001/hi，显示

```
Hello,hello!port:2001
```



 ## service-ribbon:2010 服务消费者

ribbon是一个负载均衡客户端。

1. pom.xml 文件分别引入起步依赖spring-cloud-starter-eureka、spring-cloud-starter-ribbon、spring-boot-starter-web

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-ribbon</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

2. 在工程的启动类中,通过@EnableDiscoveryClient向服务中心注册；并且向程序的ioc注入一个bean: restTemplate;并通过@LoadBalanced注解表明这个restRemplate开启负载均衡的功能。
3. 写一个测试类HelloService，通过之前注入ioc容器的restTemplate来消费hello服务的“/hello”接口，在这里我们直接用的程序名替代了具体的url地址，在ribbon中它会根据服务名来选择具体的服务实例，根据服务实例在请求的时候会用具体的url替换掉服务名，代码如下

```java
@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;

    public String hiService(String name) {
        //这里是通过服务名来调用接口
        return restTemplate.getForObject("http://hello/hello?name="+name,String.class);
    }
}
```

4. 写一个controller，在controller中用调用HelloService 的方法，代码如下

```java
@RestController
public class HelloController {
    @Autowired
    HelloService helloService;
    @RequestMapping(value = "/hi")
    public String hi(@RequestParam String name){
        return helloService.hiService(name);
    }
}
```

重复访问http://localhost:2010/hi?name=xw

结果交替显示为

```
Hello, xw! port: 2001
Hello, xw! port: 2002
```

 ## service-feign:2020服务消费者

Feign是一个声明式的伪Http客户端，它使得写Http客户端变得更简单。使用Feign，只需要创建一个接口并注解。它具有可插拔的注解特性，可使用Feign 注解和JAX-RS注解。Feign支持可插拔的编码器和解码器。Feign默认集成了Ribbon，并和Eureka结合，默认实现了负载均衡的效果。

   1. pom.xml引入Feign的起步依赖spring-cloud-starter-feign、Eureka的起步依赖spring-cloud-starter-eureka、Web的起步依赖spring-boot-starter-web

      ```xml
              <dependency>
                  <groupId>org.springframework.cloud</groupId>
                  <artifactId>spring-cloud-starter-eureka</artifactId>
              </dependency>
              <dependency>
                  <groupId>org.springframework.cloud</groupId>
                  <artifactId>spring-cloud-starter-feign</artifactId>
              </dependency>
              <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-starter-web</artifactId>
              </dependency>
      ```

   2. 在程序的启动类，加上@EnableFeignClients注解开启Feign的功能

   3. 定义一个feign接口,通过@ FeignClient（“服务名”），来指定调用哪个服务

      ```java
      @FeignClient("hello")
      public interface SchedualServiceHello {
          @RequestMapping(value = "/hello",method = RequestMethod.GET)
          String sayHiFromClientOne(@RequestParam(value = "name") String name);
      }
      ```

   3. 在Web层的controller层，对外暴露一个”/hello”的API接口,通过调用Feigin客户端SchedualServiceHello来消费服务

   ```java
@RestController
public class HelloController {

    @Autowired
    SchedualServiceHello schedualServiceHello;
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String sayHi(@RequestParam String name){
        return schedualServiceHello.sayHiFromClientOne(name);
    }
}
   ```
 ## 

## 熔断器Netflix Hystrix

### 在feigin使用断路器

1. Feign是自带断路器的，在D版本的Spring Cloud中，它没有默认打开。需要在配置文件中配置打开它，在配置文件加以下代码：

```properties
#打开熔断配置
feign.hystrix.enabled=true
```

2. 在FeiginClient的SchedualServiceHello接口的注解中加上fallback的指定类就行了

```java
@FeignClient(value = "hello",fallback = SchedualServiceHiHystric.class)
public interface SchedualServiceHello {
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}
```

3. SchedualServiceHelloHystric需要实现SchedualServiceHello接口，并注入到IOC容器中

```java
@Component
public class SchedualServiceHelloHystric implements SchedualServiceHello {
    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry， "+name;
    }
}
```

4. 启动四servcie-feign工程，浏览器打开<http://localhost:2020/hi?name=xw,注意此时hello工程没有启动，网页显示：

   ```
   sorry, xw
   ```

5. 启动hello工程，再次访问，浏览器显示：

   ```
   Hello, xw! port: 2001
   ```

###在ribbon使用断路器

1. pom.xml

```xml
<!--开启断路器-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-hystrix</artifactId>
</dependency>
```

2. 启动类 加@EnableHystrix注解开启Hystrix

3. 改造HelloService类，在hiService方法上加上@HystrixCommand注解，该注解对该方法创建了熔断器的功能，并指定了fallbackMethod熔断方法，熔断方法直接返回了一个字符串，字符串为”hi,”+name+”,sorry,error!”，代码如下：

   ```java
   @Service
   public class HelloService {

       @Autowired
       RestTemplate restTemplate;

       @HystrixCommand(fallbackMethod = "hiError")
       public String hiService(String name) {
           return restTemplate.getForObject("http://SERVICE-HELLO/hello?name="+name,String.class);
       }

       public String hiError(String name) {
           return "hi,"+name+",sorry,error!";
       }
   }


   ```

4. 启动service-ribbon，访问http://localhost:2010/hi?name=xw,浏览器显示

```
Hello, xw! port: 2001
```

5. 关闭hello工程，再访问http://localhost:2010/hi?name=xw，浏览器显示

```
hi,xw,sorry,error!
```

6. 说明当hello服务不可用的时候，service-ribbon调用hello的API接口时，会执行快速失败，直接返回一组字符串，而不是等待响应超时，这很好的控制了容器的线程阻塞。

##  Hystrix Dashboard熔断器仪表盘

基于service-ribbon、service-feigin改造

1. pom.xml

   ```xml

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>

   <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
   </dependency>
   ```

   ​

2. 在主程序启动类中加入@EnableHystrixDashboard注解，开启hystrixDashboard

3. 打开浏览器：访问http://localhost:2010/hystrix，输入http://127.0.0.1:2010/hystrix.stream，随便输入title，点击Monitor Stream按钮，会出现监控页面，反复刷新接口调用地址，会实时显示调用情况。



## zuul:1000 路由网关

Zuul的主要功能是路由转发和过滤器。路由功能是微服务的一部分，比如／api/user转发到到user服务，/api/shop转发到到shop服务。zuul默认和Ribbon结合实现了负载均衡的功能。

### 路由转发

1. pom.xml

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zuul</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

2. 在其入口applicaton类加上注解@EnableZuulProxy，开启zuul的功能
3. 配置文件application.properties加上以下的配置代码

```
#以/api-a/ 开头的请求都转发给service-ribbon服务
zuul.routes.api-a.path=/api-a/**
zuul.routes.api-a.serviceId=service-ribbon

#以/api-b/开头的请求都转发给service-feign服务；
zuul.routes.api-b.path=/api-b/**
zuul.routes.api-b.serviceId=service-feigin
```

启动两个hello应用，端口号分别为2001和2002，反复访问http://localhost:1000/api-a/hi，交替显示

```
Hello, hello! port: 2001
Hello, hello! port: 2002
```

### 服务过滤

zuul不仅只是路由，并且还能过滤，做一些安全验证。

```java
@Component
public class MyFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(MyFilter.class);
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("token");
        if(accessToken == null) {
            log.warn("token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("token is empty");
            }catch (Exception e){}

            return null;
        }
        log.info("ok");
        return null;
    }
}
```

 filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下： 
- pre：路由之前
- routing：路由之时
- post： 路由之后
- error：发送错误调用

filterOrder：过滤的顺序

shouldFilter：这里可以写逻辑判断，是否要过滤，本文true,永远过滤。

run：过滤器的具体逻辑。可用很复杂，包括查sql，nosql去判断该请求到底有没有权限访问。

## Spring Cloud Bus消息总线

Spring Cloud Bus 将分布式的节点用轻量的消息代理连接起来。它可以用于广播配置文件的更改或者服务之间的通讯，也可以用于监控。本文要讲述的是用Spring Cloud Bus实现通知微服务架构的配置文件的更改。



## 服务链路追踪（Spring Cloud Sleuth）

Spring Cloud Sleuth 主要功能就是在分布式系统中提供追踪解决方案，并且兼容支持了 zipkin，你只需要在pom文件中引入相应的依赖即可。

### zipkin:4000 链路跟踪服务端

1. 创建zipkin工程，pom.xml

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>io.zipkin.java</groupId>
            <artifactId>zipkin-server</artifactId>
        </dependency>

        <dependency>
            <groupId>io.zipkin.java</groupId>
            <artifactId>zipkin-autoconfigure-ui</artifactId>
        </dependency>
```

2. 在其程序入口类, 加上注解@EnableZipkinServer，开启ZipkinServer的功能


### hello:2001 链路跟踪客户端

1. pom.xml引入spring-cloud-starter-zipkin依赖

```
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zipkin</artifactId>
        </dependency>
```

2. application.properties中加入

```properties
#配置链路跟踪服务器地址
spring.zipkin.base-url=http://localhost:4000
```

3. 同理配置service-ribbon:2010 链路跟踪客户端、service-feigin:2020 链路跟踪客户端
4. 互相调用时，访问http://localhost:4000,点击Dependencies,可以发现服务的依赖关系;点击find traces,可以看到具体服务相互调用的数据

