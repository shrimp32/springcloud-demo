# zuul:2100 路由网关

Zuul的主要功能是路由转发和过滤器。路由功能是微服务的一部分，比如／api/user转发到到user服务，/api/shop转发到到shop服务。zuul默认和Ribbon结合实现了负载均衡的功能。
​
启动两个hello应用，端口号分别为2001和2002，反复访问http://localhost:2100/api-a/hi，交替显示

```
Hello, hello! port: 2001
Hello, hello! port: 2002
```
