package com.xw.cloud.feign;

import org.springframework.stereotype.Component;

/**
 * Created by 夏玮
 * 2018/3/20 16:05
 */
@Component
public class SchedualServiceHelloHystric implements SchedualServiceHello {
    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry, "+name;
    }
}
