# Eureka Server
- port：2000

## 安全管理
Eureka本身不具备安全认证的能力，Spring Cloud使用Spring Security为Eureka Server进行了增强。
加依赖
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

加配置
```properties
spring.security.user.name=admin
spring.security.user.password=admin
```
如不设置这段内容，账号默认是user，密码是一个随机值，该值会在启动时打印出来。

## 高可用