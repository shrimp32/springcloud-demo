# zuul:2100 路由网关
- /routes 
- /filters

## 路由功能
Zuul的主要功能是路由转发和过滤器。路由功能是微服务的一部分，比如／api/user转发到到user服务，/api/shop转发到到shop服务。zuul默认和Ribbon结合实现了负载均衡的功能。
​
启动两个hello应用，端口号分别为2001和2002，反复访问http://localhost:2100/api-a/hi，交替显示

```
Hello, hello! port: 2001
Hello, hello! port: 2002
```

## 过滤
- pre类型过滤器 PreDecorationFilter
- route类型过滤器 SendForwardFilter、RibbonRoutingFilter、SimpleHostRoutingFilter
- post类型过滤器 SendResponseFilter
- error类型过滤器 SendErrorFilter
- 自定义Zuul过滤器 MyFilter.java

## 统一异常处理
- Zuul异常处理就是由SendErrorFilter完成
- 自定义ErrorFilter，post类型，根据状态码来进行判断，是否要统一处理

>注意，对于代理的应用里的异常并不能统一处理

## zuul的熔断回退ServiceFallbackProvider.java
- Dalston及更低版本通过实现ZuulFallbackProvider 接口，从而实现回退；
- Edgware及更高版本通过实现FallbackProvider 接口，从而实现回退。
- 在Edgware中：
    - FallbackProvider是ZuulFallbackProvider的子接口。
    - ZuulFallbackProvider已经被标注Deprecated ，很可能在未来的版本中被删除。
    - FallbackProvider接口比ZuulFallbackProvider多了一个ClientHttpResponse fallbackResponse(Throwable cause); 方法，使用该方法，可获得造成回退的原因。

### 测试过程
分别访问，根据延时长短，进行熔断回退
- http://localhost:2100/api-a/test1
- http://localhost:2100/api-a/test2
- http://localhost:2100/api-a/test3

## 配置Zuul的Hystrix线程池

## 限流
策略：
- user限流
- IP限流
- url限流

### 基于过滤器和ratelimit的限流，参见RateLimitZuulFilter
RateLimiter是Google开源的实现了令牌桶算法的限流工具（速率限制器）。http://ifeve.com/guava-ratelimiter/

### 基于spring-cloud-zuul-ratelimit的限流
Spring Cloud Zuul RateLimiter结合Zuul对RateLimiter进行了封装，通过实现ZuulFilter提供了服务限流功能

限流粒度/类型:
- Authenticated User	针对请求的用户进行限流
- Request Origin	针对请求的Origin进行限流
- URL	针对URL/接口进行限流
- Service	针对服务进行限流，如果没有配置限流类型，则此类型生效

需要在pom文件中引入spring-cloud-zuul-ratelimit依赖

```yaml
zuul:

    ratelimit:

        key-prefix: your-prefix  #对应用来标识请求的key的前缀

        enabled: true

        repository: REDIS  #对应存储类型（用来存储统计信息）

        behind-proxy: true  #代理之后

        default-policy: #可选 - 针对所有的路由配置的策略，除非特别配置了policies

             limit: 10 #可选 - 每个刷新时间窗口对应的请求数量限制

             quota: 1000 #可选-  每个刷新时间窗口对应的请求时间限制（秒）

              refresh-interval: 60 # 刷新时间窗口的时间，默认值 (秒)

               type: #可选 限流方式

                    - user

                    - origin

                    - url

          policies:

                myServiceId: #特定的路由

                      limit: 10 #可选- 每个刷新时间窗口对应的请求数量限制

                      quota: 1000 #可选-  每个刷新时间窗口对应的请求时间限制（秒）

                      refresh-interval: 60 # 刷新时间窗口的时间，默认值 (秒)

                      type: #可选 限流方式

                          - user

                          - origin

                          - url

```

### 基于系统负载的动态限流

# Zuul 2核心特性

- 服务器协议

	- HTTP/2——完整的入站（inbound）HTTP/2 连接服务器支持
	- 双向 TLS（Mutual TLS）——支持在更安全的场景下运行 Zuul

- 弹性特性

自适应重试——Netflix 用于增强弹性和可用性的核心重试逻辑

源并发保护——可配置的并发限制，避免源过载，隔离 Zuul 背后的各个源

- 运营特性

请求 Passport——跟踪每个请求的所有生命周期事件，这对调试异步请求非常有用

状态分类——请求成功和失败的可能状态枚举，比 HTTP 状态码更精细

请求尝试——跟踪每个代理的尝试和状态，对调试重试和路由特别有用

- 一些即将推出的功能，包括：

Websocket/SSE——支持通道推送通知

限流和限速——防止恶意客户端连接和请求，帮助抵御大规模攻击

掉电过滤器——Zuul 过载时禁用一些 CPU 密集型特性

可配置路由——基于文件的路由配置，而不需要在 Zuul 中创建路由过滤器
