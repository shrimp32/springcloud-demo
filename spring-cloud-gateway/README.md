# Spring Cloud gateway
## 简单路由
两种配置方式：

	1. 配置文件
	2. 代码配置

在Spring Cloud Gateway中，主要有两种类型的过滤器：GlobalFilter 和 GatewayFilter
- GlobalFilter ： 全局过滤器，对所有的路由均起作用
- GatewayFilter ： 只对指定的路由起作
## 权重路由

## Spring Cloud Gateway自定义Token校验过滤器
> https://segmentfault.com/a/1190000016227780

### 场景说明：
1. 对访问网关的请求进行token校验，只有当token校验通过时，才转发到后端服务，否则直接返回401
2. token存放在redis中, key为用户的uid

### 运行说明
运行上面的代码，需要先启动redis服务，由于没有配置redis的地址和端口，默认采用localhost和6379端口，如果不一致，请自行在application.yml文件中配置即可；
当通过网关访问/user/list时，如果token验证通过，会转发到 http://localhost:8077/api/user/list 上，这是另外的一个接口服务，自行根据实际情况修改；
### 自定义GatewayFilter
两种实现方式：
1. 实现GatewayFilter接口
- 如何应用？——通过
2. 继承AbstractGatewayFilterFactory类 
- 如何应用？——在配置文件中配置路由信息

### 自定义GlobalFilter

## 限流
RequestRateLimiter

## 基于系统负载的动态限流#

