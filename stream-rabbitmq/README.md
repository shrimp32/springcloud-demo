# Spring Cloud Stream

## 简介
### 应用模型

- Middleware:一些消息中间件，本文用例使用kafka
- Binder：粘合剂，将Middleware和Stream应用粘合起来，不同Middleware对应不同的Binder。
- Channel：通道，应用程序通过一个明确的Binder与外界（中间件）通信。
- ApplicationCore：Stream自己实现的消息机制封装，包括分区、分组、发布订阅的语义，与具体中间件无关，这会让开发人员很容易地以相同的代码使用不同类型的中间件。

> Stream能自动发现并使用类路径中的binder,你也可以引入多个binders并选择使用哪一个，甚至可以在运行时根据不同的channels选择不同的binder实现。

### 分组
发布-订阅模型可以很容易地通过共享topics连接应用程序，但创建一个应用多实例的的水平扩展能力同等重要。当这样做时，应用程序的不同实例被放置在一个竞争的消费者关系中，其中只有一个实例将处理一个给定的消息，这种分组类似于Kafka consumer groups，灵感也来源于此。每个消费者通过spring.cloud.stream.bindings.<channelName>.group指定一个组名称，channelName是代码中定义好的通道名称，下文会有介绍。

消费者组订阅是持久的，如果你的应用指定了group，那即便你这个组下的所有应用实例都挂掉了，你的应用也会在重新启动后从未读取过的位置继续读取。但如果不指定groupStream将分配给一个匿名的、独立的只有一个成员的消费组，该组与所有其他组都处于一个发布－订阅关系中，还要注意的是匿名订阅不是持久的，意味着如果你的应用挂掉，那么在修复重启之前topics中错过的数据是不能被重新读取到的。所以为了水平扩展和持久订阅，建议最好指定一个消费者组。
### 分区
Stream提供了一个通用的抽象，用于统一方式进行分区处理，和具体使用的中间件无关，因此分区可以用于自带分区的代理（如kafka）或者不带分区的代理（如rabbiemq），这句话要反复读几遍。

Stream支持在一个应用程序的多个实例之间数据分区，N个生产者的数据会发送给M个消费者，并保证共同的特性的数据由相同的消费者实例处理，这会提升你处理能力。



Stream使用多实例进行分区数据处理是一个复杂设置，分区功能需要在生产者与消费者两端配置，SpringCloudDataFlow可以显著的简化过程，而且当你没有用SpringCloudDataFlow时，会给你的配置带来一些不便，需要你提前规划好，而不能再应用启动后动态追加。

下面是生产者有效的和典型的配置（Output Bindings）

spring.cloud.stream.bindings.<channelName>.producer.partitionKeyExpression=payload.id
spring.cloud.stream.bindings.<channelName>.producer.partitionCount=5
分区key的值是基于partitionKeyExpression计算得出的，用于每个消息被发送至对应分区的输出channel，partitionKeyExpression是spirng EL表达式用以提取分区键

下面是消费者有效的和典型的配置（Input Bindings）

spring.cloud.stream.bindings.input.consumer.partitioned=true
spring.cloud.stream.instanceIndex=3
spring.cloud.stream.instanceCount=5
instanceCount表示应用实例的总数，instanceIndex在多个实例中必须唯一，并介于0~（instanceCount-1）之间。实例的索引可以帮助每个实例确定唯一的接收数据的分区，正确的设置这两个值十分重要，用来确保所有的数据被消费，以及应用实例接收相互排斥不重复消费。
## 环境说明 
rabbitmq
- 版本3.7-management
- 默认端口
- 本地docker中

## 开发过程
引入pom依赖
配置binder参数
定义通道
配置通道绑定参数
通过@EnableBinding触发绑定
消费者通过@StreamListener监听
配置分区、分组信息

## 运行
运行StreamRabbitApplication或StreamRabbitmqApplicationTests

对3个通道，分别发送消息"大家好"，执行结果如下
```
2018-08-21 15:36:44.008  INFO 4223 --- [           main] c.x.c.stream.StreamRabbitmqApplication   : 字符串信息发送
2018-08-21 15:36:44.021  INFO 4223 --- [           main] com.xw.cloud.stream.SinkReceiver         : Received1:大家好,output1,There is mychannel
2018-08-21 15:36:44.021  INFO 4223 --- [           main] com.xw.cloud.stream.SinkReceiver         : msg:receipt msg :大家好,output1
2018-08-21 15:36:44.022  INFO 4223 --- [           main] com.xw.cloud.stream.SinkReceiver         : Received2:大家好,output2,There is input
2018-08-21 15:36:44.022  INFO 4223 --- [           main] com.xw.cloud.stream.SinkReceiver         : Received3:大家好,output3,There is input3
```


