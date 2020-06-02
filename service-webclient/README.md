# Service-Webclient
- 服务消费者
    - webclient方式调用服务，非阻塞
    - restTemplate方式调用，阻塞
- 使用hystrix作为断路器:

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
```
- admin client 
- webflux

## 接口说明
需要先启动service-hello服务和注册中心服务
- /test :通过webclient调用service-hello/hello服务
- /slow :提供延迟的接口
- /tweets-blocking :webclient方式调用
- /tweets-non-blocking ：restTemplate方式调用

## 运行结果
访问/tweets-blocking,日志如下
```
Starting BLOCKING Controller!
Tweet(text=RestTemplate rules, username=@user1)
Tweet(text=WebClient is better, username=@user2)
Tweet(text=OK, both are useful, username=@user1)
Exiting BLOCKING Controller!
```
访问/tweets-non-blocking,日志输出如下：
```
Starting NON-BLOCKING Controller!
Exiting NON-BLOCKING Controller!
Tweet(text=RestTemplate rules, username=@user1)
Tweet(text=WebClient is better, username=@user2)
Tweet(text=OK, both are useful, username=@user1)
```
请注意，此端点方法在收到响应之前已完成。
## 参考资料
- https://www.baeldung.com/spring-webclient-resttemplate
- https://www.baeldung.com/spring-5-webclient
