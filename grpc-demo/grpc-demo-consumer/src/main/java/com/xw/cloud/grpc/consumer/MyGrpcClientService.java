package com.xw.cloud.grpc.consumer;

import com.xw.cloud.grpc.lib.GreeterGrpc;
import com.xw.cloud.grpc.lib.GreeterOuterClass;
import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class MyGrpcClientService {

    @GrpcClient("grpc-demo-provider")
    private Channel serverChannel;

    public String sendMessage(String name) {
        GreeterGrpc.GreeterBlockingStub stub= GreeterGrpc.newBlockingStub(serverChannel);
        GreeterOuterClass.HelloReply response = stub.sayHello(GreeterOuterClass.HelloRequest.newBuilder().setName(name).build());
        return response.getMessage();
    }
}
