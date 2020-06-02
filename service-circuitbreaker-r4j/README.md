# Service-r4j
- 服务消费者
    - restTemplate方式调用，阻塞
    - 使用resilience4j作为断路器
- admin client 

## 接口说明
需要先启动service-hello服务和注册中心服务
- /test :通过webclient调用service-hello/hello服务
- block/slow :提供延迟的接口
- block/tweets : restTemplate方式调用

## 注意
项目启动时，注入了2个RestTemplate，一个用于调用微服务提供的接口，一个用于调用`https://httpbin.org`第三方接口

@Balance只能用于调用注册到注册中心的服务
