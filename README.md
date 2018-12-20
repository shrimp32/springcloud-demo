# Spring Cloud Demo 应用说明
关键点：

- 服务注册与发现（eureka）
- 服务消费与负载均衡：Spring Cloud可以用RestTemplate+Ribbon和Feign来调用服务，实现服务的负载均衡
- 断路器（Netflix Hystrix）
- 网关和智能路由（Zuul）
- 配置中心（Spring Cloud Config）
- 消息总线（Spring Cloud Bus）
- 服务链路追踪（Spring Cloud Sleuth）
- Spring Cloud Stream 

##  一、服务注册中心

  ### 服务端配置eureka server:2000
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
2. application.properties

```properties
    #配置eureka服务端的访问地址
    eureka.client.serviceUrl.defaultZone=http://localhost:2000/eureka/
```

## 二、cloud-config:3000 配置中心

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

4. 在远程仓库上创建hello应用的配置文件，service-hello-dev.properties如下：

```
app.name=hello
app.version=1.0

spring.application.name=service-hello
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

2. 在程序的配置文件bootstrap.properties文件配置以下,注意配置文件名

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



## 三、服务消费与负载均衡

 ### service-ribbon:2010 服务消费者

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
        return restTemplate.getForObject("http://service-hello/hello?name="+name,String.class);
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

 ### service-feign:2020服务消费者

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
      @FeignClient("service-hello")
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

## 四、熔断器Netflix Hystrix

### 在feigin使用断路器

1. Feign是自带断路器的，在D版本的Spring Cloud中，它没有默认打开。需要在配置文件中配置打开它，在配置文件加以下代码：

```properties
#打开熔断配置
feign.hystrix.enabled=true
```

2. 在FeiginClient的SchedualServiceHello接口的注解中加上fallback的指定类就行了

```java
@FeignClient(value = "service-hello",fallback = SchedualServiceHiHystric.class)
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
3. 改造HelloService类，在`hiService`方法上加上`@HystrixCommand`注解，该注解对该方法创建了熔断器的功能，并指定了`fallbackMethod`熔断方法，熔断方法直接返回了一个字符串，字符串为`”hi,”+name+”,sorry,error!”`，代码如下：

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

###  hystrix-dashboard:2800熔断器仪表盘

可以基于service-ribbon、service-feign改造，进行仪表盘的展示，也可以新建一个项目展示仪表盘。

新建项目hystrix-dashboard，端口2800

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

2. 在主程序启动类中加入`@EnableHystrixDashboard`注解，开启hystrixDashboard
3. 打开浏览器：访问`http://localhost:2800/hystrix`，输入`http://127.0.0.1:2010/hystrix.stream`，随便输入title，点击Monitor Stream按钮，会出现监控页面，反复刷新接口调用地址，会实时显示调用情况。
4. 同理，如果要监控service-feign应用，输入`http://127.0.0.1:2020/hystrix.stream`，随便输入title，点击Monitor Stream按钮即可。

### turbine:2810 聚合仪表盘

在实际系统中，一个服务往往有多个实例，可以通过turbine进行聚合监控。

新建项目turbine:2810

1. pom.xml

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-turbine</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-netflix-turbine</artifactId>
</dependency>

```

2. 配置文件

```properties
server.port=2820
spring.application.name=turbine

#需要收集监控信息的服务名
turbine.app-config=service-ribbon,service-feign
#集群名称为default，当我们服务数量非常多的时候，可以启动多个Turbine服务来构建不同的聚合集群，
#而该参数可以用来区分这些不同的聚合集群，同时该参数值可以在Hystrix仪表盘中用来定位不同的聚合集群
#只需要在Hystrix Stream的URL中通过cluster参数来指定；
turbine.cluster-name-expression="default"
#设置为true，可以让同一主机上的服务通过主机名与端口号的组合来进行区分，默认情况下会以host来区分不同的服务，这会使得在本地调试的时候，本机上的不同服务聚合成一个服务来统计。
turbine.combine-host-port=true
```

3. 启动service-ribbon和hystrix-dashboard以及turbine，打开监控面板http://localhost:2800/hystrix，输入监控地址。

```
http://localhost:2810/turbine.stream
```

点击监控即可查看，可以看到多组服务的监控情况。

## 五、zuul:2100 路由网关

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

启动两个hello应用，端口号分别为2001和2002，反复访问http://localhost:2100/api-a/hi，交替显示

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

## 六、Spring Cloud Bus消息总线

Spring Cloud Bus 将分布式的节点用轻量的消息代理连接起来。它可以用于广播配置文件的更改或者服务之间的通讯，也可以用于监控。本文要讲述的是用Spring Cloud Bus实现通知微服务架构的配置文件的更改。



## 七、服务链路追踪（Spring Cloud Sleuth）

Spring Cloud Sleuth 主要功能就是在分布式系统中提供追踪解决方案，并且兼容支持了 zipkin，你只需要在pom文件中引入相应的依赖即可。

### zipkin:4000 链路跟踪服务端

1. 创建zipkin工程，pom.xml

```xml
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

`spring-cloud-starter-zipkin`依赖内部包含了两个依赖，等于同时引入了`spring-cloud-starter-sleuth`，`spring-cloud-sleuth-zipkin`两个依赖。

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
spring.sleuth.enabled=true
#sleuth的采样率
spring.sleuth.sampler.percentage=1
```

3. 控制台输出类似[hello,d251f40af64361d2,e46132755dc395e1,true] 分别代表了[应用名称，traceId，spanId，当前调用是否被采集]。
4. 同理配置service-ribbon:2010 链路跟踪客户端、service-feigin:2020 链路跟踪客户端
5. 互相调用时，访问http://localhost:4000,点击Dependencies,可以发现服务的依赖关系;点击find traces,可以看到具体服务相互调用的数据

## 八、Spring Cloud Stream

Spring Cloud Stream本质上就是整合了Spring Boot和Spring Integration，实现了一套轻量级的消息驱动的微服务框架。通过使用Spring Cloud Stream，可以有效地简化开发人员对消息中间件的使用复杂度。

### 应用模型

- Middleware:一些消息中间件，如kafka、RabbitMQ
- Binder：粘合剂，将Middleware和Stream应用粘合起来，不同Middleware对应不同的Binder。
- Channel：通道，应用程序通过一个明确的Binder与外界（中间件）通信。
- ApplicationCore：Stream自己实现的消息机制封装，包括分区、分组、发布订阅的语义，与具体中间件无关，这会让开发人员很容易地以相同的代码使用不同类型的中间件。

> Stream能自动发现并使用类路径中的binder,你也可以引入多个binders并选择使用哪一个，甚至可以在运行时根据不同的channels选择不同的binder实现。

### 消费者分组

每个消费者通过`spring.cloud.stream.bindings.<channelName>.group`指定一个组名称，`channelName`是代码中定义好的通道名称。

消费者组订阅是持久的，如果你的应用指定了`group`，那即便你这个组下的所有应用实例都挂掉了，你的应用也会在重新启动后从未读取过的位置继续读取。但如果不指定`group`Stream将分配给一个匿名的、独立的只有一个成员的消费组，该组与所有其他组都处于一个发布－订阅关系中，还要注意的是匿名订阅不是持久的，意味着如果你的应用挂掉，那么在修复重启之前topics中错过的数据是不能被重新读取到的。所以为了水平扩展和持久订阅，建议最好指定一个消费者组。

### 分区

Stream提供了一个通用的抽象，用于统一方式进行分区处理，和具体使用的中间件无关，因此分区可以用于自带分区的代理（如kafka）或者不带分区的代理（如rabbiemq）。Stream支持在一个应用程序的多个实例之间数据分区，N个生产者的数据会发送给M个消费者，并保证共同的特性的数据由相同的消费者实例处理，这会提升你处理能力。

Stream使用多实例进行分区数据处理是一个复杂设置，分区功能需要在生产者与消费者两端配置，SpringCloudDataFlow可以显著的简化过程，而且当你没有用SpringCloudDataFlow时，会给你的配置带来一些不便，需要你提前规划好，而不能再应用启动后动态追加。

**下面是生产者有效的和典型的配置（Output Bindings）**

```
spring.cloud.stream.bindings.<channelName>.producer.partitionKeyExpression=payload.id
spring.cloud.stream.bindings.<channelName>.producer.partitionCount=5
```

分区key的值是基于partitionKeyExpression计算得出的，用于每个消息被发送至对应分区的输出channel，partitionKeyExpression是spirng EL表达式用以提取分区键

**下面是消费者有效的和典型的配置（Input Bindings）**

```
spring.cloud.stream.bindings.input.consumer.partitioned=true
spring.cloud.stream.instanceIndex=3
spring.cloud.stream.instanceCount=5
```

`instanceCount`表示应用实例的总数，`instanceIndex`在多个实例中必须唯一，并介于0~（`instanceCount`-1）之间。实例的索引可以帮助每个实例确定唯一的接收数据的分区，正确的设置这两个值十分重要，用来确保所有的数据被消费，以及应用实例接收相互排斥不重复消费。

## 九、Spring Boot Admin监控中心

SpringBootAdmin的Server端也可以注册在eureka这样的管理中心上，好处是可以监控所有注册在eureka上的服务，SpringBootAdmin客户端无需再配置服务端，注册到Eureka上后，SpringBootAdmin可以定时拉取服务注册列表，无需再为服务节点配置监控中心的地址。

### admin server:5000

1. 引入依赖

   ```xml
   <!--引入admin server依赖-->
   <dependency>
      <groupId>de.codecentric</groupId>
      <artifactId>spring-boot-admin-server</artifactId>
      <version>1.5.6</version>
   </dependency>
   <!--admin server的展示-->
   <dependency>
      <groupId>de.codecentric</groupId>
      <artifactId>spring-boot-admin-server-ui</artifactId>
      <version>1.5.6</version>
   </dependency>
   <!--作为注册到eureka的应用-->
   <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-eureka</artifactId>
   </dependency>
   ```
```

2. 修改配置文件

​```properties
server.port=5000
spring.application.name=admin

#配置eureka服务端的访问地址
eureka.client.serviceUrl.defaultZone=http://localhost:2000/eureka/
```

访问地址：http://localhost:5000

### admin client

修改hello应用，作为admin client

1. 引入admin client的依赖包

```xml
<!--作为spring boot admin的客户端-->
<dependency>
   <groupId>de.codecentric</groupId>
   <artifactId>spring-boot-admin-starter-client</artifactId>
   <version>1.5.6</version>
</dependency>
```

2. 修改配置文件

```
# 作为spring boot admin的客户端
# 如果admin注册到了eureka上，可以自动获取所有eureka上的服务，进行监控，不用单独配置客户端
# spring.boot.admin.url=http://localhost:5000

# 默认的监控端点是 关闭的，设置为false后，才可访问
management.security.enabled=false
```

##  十、微服务的认证授权

zuul+oauth2 server解决方案



### oauth server：2600



##  十一、docker部署

### 构建eureka server镜像

1. pom.xml

```xml
<build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <!-- tag::plugin[] -->
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>0.4.3</version>
                <configuration>
                	<!-- 镜像名称 -->
                    <imageName>${docker.image.prefix}/${project.artifactId}</imageName>
                    <!-- Dockerfile 的位置 -->
                    <dockerDirectory>src/main/docker</dockerDirectory>
                    <!--resources是指那些需要和 Dockerfile 放在一起，在构建镜像时使用的文件，一般应用 jar 包需要纳入-->
                    <resources>
                        <resource>
                            <targetPath>/</targetPath>
                            <directory>${project.build.directory}</directory>
                            <include>${project.build.finalName}.jar</include>
                        </resource>
                    </resources>
                </configuration>
            </plugin>
            <!-- end::plugin[] -->
        </plugins>
    </build>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <docker.image.prefix>xw.cloud</docker.image.prefix>
    </properties>
```

2. 修改配置文件

   ```properties
       spring.application.name=eureka
       server.port=2000
       eureka.instance.prefer-ip-address=true
       
       #server本身不作为客户端注册   
       eureka.client.register-with-eureka=false
       eureka.client.fetch-registry=false
       eureka.client.serviceUrl.defaultZone=http://eureka:${server.port}/eureka/
   ```

   ​

3. 编写Dockerfile文件，创建src/main/docker/Dockerfile文件

```
FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD eureka-server-0.0.1-SNAPSHOT.jar app.jar
#RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 2000
```

3.  构建镜像

```
mvn clean package docker:build
```

### 构建hello应用镜像

1. pom.xml同上
2. 修改配置文件，defaultZone发现服务的host改为镜像名

```properties
    spring.application.name=hello
    server.port=2001
        
    #配置eureka服务端的访问地址
    eureka.client.serviceUrl.defaultZone=http://eureka:2000/eureka/
```

3. dockefile 编写同上
4. 构建镜像
5. 运行docker镜像

```
docker run -p 2000: 2000 -t xw.cloud/eureka
docker run -p 2001: 2001 -t xw.cloud/hello
```

 访问localhost:2000

### 采用docker-compose启动镜像

采用docker-compose的方式编排镜像，启动镜像

```
version: '3'
services:
  eureka:
    image: xw.cloud/eureka
    restart: always
    ports:
      - 2000:2000

  hello:
    image: xw.cloud/service-hi
    restart: always
    ports:
      - 2001:2001
```

输入命令： docker-compose up

### 采用docker-compose编排并启动镜像

现在以eureka-server为例： 
将Dockerfile移到eureka-server的主目录，改写ADD的相对路径：

```
FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD ./target/eureka-server-0.0.1-SNAPSHOT.jar app.jar
#RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 87611234567
```

同理修改service-hi目录；

编写构建镜像docker-compose-dev.yml文件：

```
version: '3'
services:
  eureka-server:
    build: eureka
    ports:
      - 2000:2000

  service-hi:
    build: hello
    ports:
      - 2001:2001
```

命令构建镜像并启动：

```
docker-compose -f docker-compose.yml -f docker-compose-dev.yml up 
```