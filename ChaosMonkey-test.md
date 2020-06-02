# Codecentric Chaos Monkey Sample

## 示例说明
示例包含2个服务，分布是：
- consul-consumer
- consul-producer

## 运行
1. 启动consul：`consul agent -dev`
2. 启动服务
```
java -jar ./target/consul-producer-0.0.1-SNAPSHOT.jar --spring.profiles.active=chaos-monkey
```