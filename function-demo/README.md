# Spring Cloud Function Demo


`spring-cloud-starter-function-web`将Function发布为web

- 通过包扫描
```properties
# 自动扫描并注入
spring.cloud.function.scan.packages=xw.cloud.function.fun
```
- 通过@Bean注入

## HelloWorld示例
- 开发一个HelloWorldFunction
- 访问示例
    - `/helloWorldFunction/{xiaw}`，通过包扫描注入
    - `/say/{xiaw}`，通过@Bean注入
