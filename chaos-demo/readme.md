## Chaos Monkey for Spring Boot Microservices
### 官网资料
- 源码 https://github.com/codecentric/chaos-monkey-spring-boot
- https://codecentric.github.io/chaos-monkey-spring-boot/
### 项目说明 
- 本项目地址 https://github.com/piomin/sample-spring-chaosmonkey
- Detailed description can be found here: [Chaos Monkey for Spring Boot Microservices](https://piotrminkowski.wordpress.com/2018/05/23/chaos-monkey-for-spring-boot-microservices/)
- 中文版：https://www.jianshu.com/p/fc54dd6d2e95 

### 运行
```
java -jar target/discovery-service-1.0-SNAPSHOT.jar

java -jar target/order-service-1.0-SNAPSHOT.jar --spring.profiles.active=chaos-monkey
java -jar -Dserver.port=9091 target/order-service-1.0-SNAPSHOT.jar --spring.profiles.active=chaos-monkey

java -jar target/product-service-1.0-SNAPSHOT.jar --spring.profiles.active=chaos-monkey
java -jar -Dserver.port=9092 target/product-service-1.0-SNAPSHOT.jar --spring.profiles.active=chaos-monkey

java -jar target/customer-service-1.0-SNAPSHOT.jar --spring.profiles.active=chaos-monkey
java -jar -Dserver.port=9093 target/customer-service-1.0-SNAPSHOT.jar --spring.profiles.active=chaos-monkey
```