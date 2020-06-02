# spring boot grpc demo
## 模块组成
### grpc-demo-lib
gRPC接口，包含原始proto文件，并负责将其转换为java代码.定义服务接口。

需要将proto文件转换成java类，故需依赖于protobuf-maven-plugin插件，另外还需要io.grpc下的grpc-all，否则生成的java类会提示缺少依赖
### grpc-demo-provider
- 服务提供者，当于serviceImpl(service的实现类)
- 使用@ GrpcService自动创建并运行一个 gRPC 服务
- 配置文件
```yaml
grpc:
  server:
    port: 9090
    address: 0.0.0.0
```
### grpc-demo-consumer
- 服务消费者，调用
- 使用@ GrpcService自动创建并运行一个 gRPC 客户端

## 运行测试
- mvn clean install
- 启动consul
- 分别启动grpc-demo-provider、grpc-demo-consumer
- 访问http://localhost:2040/?name=dd,显示'Hello,dd'

## grpc-srping-boot-starter
```xml
<!--gRPC server+client-->
<dependency>
  <groupId>net.devh</groupId>
  <artifactId>grpc-spring-boot-starter</artifactId>
  <version>2.4.0.RELEASE</version>
</dependency>
<!--gRPC server-->
<dependency>
  <groupId>net.devh</groupId>
  <artifactId>grpc-server-spring-boot-starter</artifactId>
  <version>2.4.0.RELEASE</version>
</dependency>
<!--gRPC client-->
<dependency>
  <groupId>net.devh</groupId>
  <artifactId>grpc-client-spring-boot-starter</artifactId>
  <version>2.4.0.RELEASE</version>
</dependency>
```
2.x.x.RELEASE 支持 Spring Boot 2.1+ & Spring Cloud Greenwich。

最新的版本：2.4.0.RELEASE

(使用 2.4.0.RELEASE 版本可以支持 Spring Boot 2.0.X & Spring Cloud Finchley).

1.x.x.RELEASE 支持 Spring Boot 1 & Spring Cloud Edgware 、Dalston、Camden。

最新的版本：1.4.2.RELEASE

注意: 此项目也可以在没有 Spring-Boot 的场景下使用，但需要手动的配置相关的 bean。

## 注意事项
### grpc和netty版本的兼容问题
在你深入去查问题之前，请先确保 grpc 跟 netty 的版本是彼此兼容的。 当前项目自带的依赖会确保 grpc 和 netty 是能一起正常工作。 但是在某些情况下，你可能需要用到其他库（如 tcnative ）或其他依赖项需要用到不同的 netty 版本，这就可能会造成版本冲突。 

为了防止此类问题，gRPC 和 我们建议你使用 grpc-netty-shaded 依赖。 如果你使用 (non-shaded) grpc-netty，请查看表格中（https://github.com/grpc/grpc-java/blob/master/SECURITY.md#netty）展示的grpc-java 的兼容版本。

### SSL问题
默认情况下，gRPC 客户端假定服务器使用的 TLS，并尝试使用安全连接，在开发和测试期间，一般都不需要证书，你可以切换到 PLAINTEXT 的连接方式。
```
grpc.client.(gRPC server name).negotiationType=PLAINTEXT
```
注意: 在生产环境，我们强烈推荐你使用 TLS 的模式。

### 版本问题
- grpc-srping-boot-starter 2.4.0.RELEASE
'''xml
        <os.plugin.version>1.6.0</os.plugin.version>
        <grpc.version>1.20.0</grpc.version>
        <protoc.version>3.7.1</protoc.version>
        <protobuf.plugin.version>0.6.1</protobuf.plugin.version>
'''
## todo list
- 客户端认证 security
## 参考资料
- gRPC 官方文档中文版 http://doc.oschina.net/grpc?t=60134
- https://www.oschina.net/p/grpc-spring-boot-starter/related
- gRPC Spring Boot Starter官方 https://github.com/yidongnan/grpc-spring-boot-starter/blob/master/README-zh.md