package com.xw.cloud.hello.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by 夏玮
 * 2018/2/24 16:50
 */

@RestController
public class HelloController {
    @Value("${server.port}")
    String port;

    @Value("${app.name}")
    String name;

    @RequestMapping(value = "/hello" ,method = RequestMethod.GET)
    public String sayHello(@RequestParam String name) {
        String r = "Hello, " + name +"! port: " + port;
        return r;
    }

    @RequestMapping(value = "/hi")
    public String hi(){
        String r = "Hello, " + name +"! port: " + port;
        return r;
    }

    @RequestMapping(value = "/" ,method = RequestMethod.GET)
    public String index(HttpSession session,@Value("${spring.application.name}") String name){
        String sessionId = (String)session.getAttribute("sessionId");
        if (null==sessionId) session.setAttribute("sessionId",session.getId());
        return "Welcome to "+name+"! sessionId is："+session.getId();
    }

    @RequestMapping(value = "/logout" ,method = RequestMethod.GET)
    public String logout(HttpSession session){
        session.removeAttribute("sessionId");
        return "Hello! sessionId is："+session.getAttribute("sessionId");
    }


    @GetMapping("/test1")
    public String test1() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "test1";
    }

    @GetMapping("/test2")
    public String test2() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "test2";
    }

    @GetMapping("/test3")
    public String test3() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "test3";
    }
    @GetMapping("/test4")
    public String test4() {
        int num = 199/0;
        return String.valueOf(num);
    }
}
