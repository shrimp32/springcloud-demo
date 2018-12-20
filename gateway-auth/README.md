# zuul:2100 带权限控制的路由网关

## 路由功能
Zuul的主要功能是路由转发和过滤器。路由功能是微服务的一部分，比如／api/user转发到到user服务，/api/shop转发到到shop服务。zuul默认和Ribbon结合实现了负载均衡的功能。
​
启动两个hello应用，端口号分别为2001和2002，反复访问http://localhost:2100/api-a/hi，交替显示

```
Hello, hello! port: 2001
Hello, hello! port: 2002
```

## 过滤


## 统一异常处理?
Zuul异常处理就是由SendErrorFilter完成。
- 首先，我们必须禁用默认的SendErrorFilter，官方已经提供了开关配置，直接配置即可
```
zuul.SendErrorFilter.post.disable=true
```
- 自定义ErrorFilter