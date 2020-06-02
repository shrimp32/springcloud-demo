package xw.cloud.hello.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by 夏玮
 * 2018/2/24 16:50
 */

@RestController
public class HelloController {
    @Value("${server.port}")
    String port;

    @Value("${app.name:defalutServiceName}")
    String name;

    @RequestMapping(value = "/" ,method = RequestMethod.GET)
    public String index() {
        String r = "Hello, " + name +"! port: " + port;
        return r;
    }

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

    /**
     * 获取服务实例的注册信息
     */
    @Autowired
    DiscoveryClient discoveryClient;
    @Value("${spring.application.name}")
    String serviceId;

    @GetMapping("/instance")
    public List<ServiceInstance> showInfo(){
        return this.discoveryClient.getInstances(serviceId);
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
