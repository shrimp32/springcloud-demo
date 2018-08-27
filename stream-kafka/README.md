# Stream-kafka
实现消息发送和接收的简单例子。

## 环境
- Spring Cloud Edgware.SR2
- kafka+zookeeper ：本机docker上运行

## 配置
配置输入和输出通道的主题为test_spring_stream，就可以接收消息
```properties
spring.cloud.stream.bindings.mychannel.destination=test_spring_stream
spring.cloud.stream.bindings.mysource.destination=test_spring_stream
```

## kafka和rabbitmq切换
本项目的代码，在rabbitmq中完全适用，只需要在pom.xml中修改
```xml
<!--rabbitmq-->
        <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-stream-rabbit</artifactId>
		</dependency>
<!--kafka-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-stream-kafka</artifactId>
        </dependency>		
```
##问题
- rabbitmq is ok
- 在kafka下报错信息如下：
发送消息的时候报错了
```
Exception thrown when sending a message with key='null' and payload='{-1, 1, 11, 99, 111, 110, 116, 101, 110, 116, 84, 121, 112, 101, 0, 0, 0, 12, 34, 116, 101, 120, 116...' to topic test_spring_stream:
```

