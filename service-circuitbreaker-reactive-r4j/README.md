# service-circuitbreaker-reactive-r4j
- 服务消费者
    - webclient方式调用服务，非阻塞
    - restTemplate方式调用，阻塞
- 使用resilience4j作为断路器:注意引入

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-circuitbreaker-reactor-resilience4j</artifactId>
        </dependency>
```
- admin client 
- webflux

## 接口说明
需要先启动service-hello服务和注册中心服务
- slow :提供延迟的接口
- /block/:webclient方式调用
- /noblock/：restTemplate方式调用

## 注意
项目启动时，注入了2个WebClient.Builder，一个用于调用微服务提供的接口，一个用于调用`https://httpbin.org`第三方接口
@Balance只能用于调用注册到注册中心的服务

