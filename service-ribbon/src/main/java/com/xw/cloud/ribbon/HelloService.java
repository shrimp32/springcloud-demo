package com.xw.cloud.ribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by 夏玮
 * 2018/3/16 12:49
 */
@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;

    public String hiService(String name) {
        //这里是通过服务名来调用接口
        return restTemplate.getForObject("http://service-hello/hello?name="+name,String.class);
    }

}
