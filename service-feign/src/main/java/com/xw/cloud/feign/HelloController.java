package com.xw.cloud.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by 夏玮
 * 2018/3/16 13:44
 */
@RequestMapping
public class HelloController {
    @Autowired
    SchedualServiceHello schedualServiceHello;
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    public String sayHi(@RequestParam String name){
        return schedualServiceHello.sayHiFromClientOne(name);
    }
}
