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


## 统一异常处理
- Zuul异常处理就是由SendErrorFilter完成
- 自定义ErrorFilter，post类型，根据状态码来进行判断，是否要统一处理

>注意，对于代理的应用里的异常并不能统一处理

## zuul的熔断回退
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
- 用户限流
- IP限流
- 接口限流

### 基于过滤器和ratelimit的限流，参见RateLimitZuulFilter
### 基于spring-cloud-zuul-ratelimit的限流
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