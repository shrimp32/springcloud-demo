# Spring Boot Admin Server
> 官方资料 https://github.com/codecentric/spring-boot-admin
## 使用说明
- port：5000
-

### 1. 不使用服务发现

### 2. 使用服务发现：注册到eureka上

可以自动获取eureka server上的服务，并自动监控，客户端不需要配置amdin server的信息

### 3. 集成安全
#### admin server 安全控制
'spring.profiles.active=secure`
激活applicat-secure.yml，启用basic登录认证

#### admin client 安全控制
客户端对/actuator进行了保护，需要配置
```properties
spring:
  boot:
    admin:
      client:
        url: http://localhost:5000/ #spring admin server访问地址
        username: admin #spring admin server用户名
        password: admin #spring admin server密码
        instance:
          metadata:
            user.name: ${spring.security.user.name} #客户端元数据访问用户
            user.password: ${spring.security.user.password} #客户端元数据访问密码
            user.roles: ACTUATOR_ADMIN
          prefer-ip: true
```

### 4. 邮件报警



## 功能特性

常见的功能或者监控如下：

- 显示健康状况
- 显示详细信息，例如
  JVM和内存指标
  micrometer.io指标
  数据源指标
  缓存指标
  显示构建信息编号
  关注并下载日志文件
  查看jvm系统和环境属性
  查看Spring Boot配置属性
  支持Spring Cloud的postable / env-和/ refresh-endpoint
  轻松的日志级管理
  与JMX-beans交互
  查看线程转储
  查看http跟踪
  查看auditevents
  查看http-endpoints
  查看计划任务
  查看和删除活动会话（使用spring-session）
  查看Flyway / Liquibase数据库迁移
  下载heapdump
  状态变更通知（通过电子邮件，Slack，Hipchat，…）
- 状态更改的事件日志（非持久性）

！