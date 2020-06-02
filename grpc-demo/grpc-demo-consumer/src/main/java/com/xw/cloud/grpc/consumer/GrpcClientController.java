package com.xw.cloud.grpc.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrpcClientController {

    @Autowired
    private MyGrpcClientService myGrpcClientService;

    @RequestMapping("/")
    public String printMessage(@RequestParam(defaultValue = "gRPC World") String name) {
        return myGrpcClientService.sendMessage(name);
    }
}
