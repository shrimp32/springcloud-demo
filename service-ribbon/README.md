# service-ribbon：基于ribbon的服务消费者
- port:2010

## 服务消费者
- `/hi?name=` 调用hello服务的/hello接口
- `/` 默认说明

## Ribbon客户端（消费端）负载均衡
Ribbon是Netflix发布的负载均衡器，它可以帮我们控制HTTP和TCP客户端的行为。只需为Ribbon配置服务提供者地址列表，Ribbon就可基于负载均衡算法计算出要请求的目标服务地址。

Ribbon默认为我们提供了很多的负载均衡算法，例如轮询、随机、响应时间加权等——当然，为Ribbon自定义负载均衡算法也非常容易，只需实现IRule 接口即可。
## 作为eureka客户端
## 作为admin 客户端
## 使用断路器
1. pom.xml

```xml
<!--开启断路器-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-hystrix</artifactId>
</dependency>
```

2. 启动类 加@EnableHystrix注解开启Hystrix
3. 改造HelloService类，在hiService方法上加上@HystrixCommand注解，该注解对该方法创建了熔断器的功能，并指定了fallbackMethod熔断方法，熔断方法直接返回了一个字符串，字符串为”hi,”+name+”,sorry,error!
4. 启动service-ribbon，访问http://localhost:2010/hi?name=xw,浏览器显示

```
Hello, xw! port: 2001
```

5. 关闭hello工程，再访问http://localhost:2010/hi?name=xw，浏览器显示

```
hi,xw,sorry,error!
```

6. 说明当hello服务不可用的时候，service-ribbon调用hello的API接口时，会执行快速失败，直接返回一组字符串，而不是等待响应超时，这很好的控制了容器的线程阻塞。

## Hystrix Dashboard
1. @EnableHystrixDashboard,引入spring-cloud-starter-netflix-hystrix-dashboard依赖

2. 访问Hystrix Dashboard http://localhost:2010/hystrix

3. 在控制面板输入监控端点地址：

	- E版本通过/hystrix或/hystrix.stream端点监控
	- G版本通过/actuator/hystrix.stream端点监控