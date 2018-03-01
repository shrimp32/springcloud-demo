package com.xw.cloud.hello.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 夏玮
 * 2018/2/24 16:50
 */

@RestController
public class HelloController {
    @Value("${server.port}")
    String port;

    @RequestMapping(value = "/hello" ,method = RequestMethod.GET)
    public String sayHello(@RequestParam String name) {
        String r = "Hello, " + name +"! port: " + port;
        return r;
    }

    @RequestMapping(value = "/index" ,method = RequestMethod.GET)
    public String index(){
        return "Hello!";
    }

}
