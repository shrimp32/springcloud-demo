package com.xw.cloud.grpc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GrpcSpringCloudProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcSpringCloudProviderApplication.class, args);
    }
}
