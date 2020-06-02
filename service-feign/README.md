# service-feign：基于feign的服务消费者
- port:2020
Feign是申明式客户端，整合了Ribbon

## 服务调用者/消费者
- /hi?name=xw:调用hello服务的/hello接口

## eureka客户端
@EnableDiscoveryClient

## admin 客户端
## 使用断路器
```java
@EnableFeignClients
@EnableCircuitBreaker
```

1. Feign是自带断路器的，在D版本的Spring Cloud中，它没有默认打开。需要在配置文件中配置打开它，在配置文件加以下代码：

```properties
#打开熔断配置
feign.hystrix.enabled=true
```

1. 在FeiginClient的SchedualServiceHello接口的注解中加上fallback的指定类就行了

```java
@FeignClient(value = "service-hello",fallback = SchedualServiceHiHystric.class)
public interface SchedualServiceHello {
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}
```

1. SchedualServiceHelloHystric需要实现SchedualServiceHello接口，并注入到IOC容器中

```java
@Component
public class SchedualServiceHelloHystric implements SchedualServiceHello {
    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry， "+name;
    }
}
```

1. 启动四servcie-feign工程，浏览器打开<http://localhost:2020/hi?name=xw,注意此时hello工程没有启动，网页显示：

   ```
   sorry, xw
   ```

2. 启动hello工程，再次访问，浏览器显示：

   ```
   Hello, xw! port: 2001
   ```
## feign的自定义配置

FeignClient的默认超时时间为10s，不会开启重试机制，需要自定义配置。