# service-hello 一个普通的服务提供者应用
端口号：
- 2001
- 2002
## 服务提供者
- /hello：被调用的服务，传入name参数，显示`Hello,传入的name!port:端口号`
- /hi：显示`Hello，应用名！port：端口号`
- /：显示`Hello`

## eureka client
​​1. pom.xml
  
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
3. 启动类上加`@@EnableEurekaClient`或`@EnableDiscoveryClient`
 
## config client
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
Hello,service-hello!port:2001
```

## 链路跟踪客户端
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
sleuth.enabled=true
#sleuth的采样率
sleuth.sampler.percentage=1
```
3. 控制台输出类似[hello,d251f40af64361d2,e46132755dc395e1,true] 分别代表了[应用名称，traceId，spanId，当前调用是否被采集]。
4. 同理配置service-ribbon:2010 链路跟踪客户端、service-feigin:2020 链路跟踪客户端
5. 互相调用时，访问http://localhost:4000,点击Dependencies,可以发现服务的依赖关系;点击find traces,可以看到具体服务相互调用的数据

## admin server的客户端
当spring admin server注册到eureka后，可以自动对eureka上注册的所有服务进行监控。
注册到eureka，不需要配置admin server的地址等信息。
1. pom.xml中开启监控
```xml
		<!--开启监控-->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
```
2. 配置文件修改
```properties
# 关闭权限，使得管理的各端点可访问
management.security.enabled=false
```