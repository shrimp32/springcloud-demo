package com.xw.cloud.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by 夏玮
 * 2018/3/16 13:42
 */
@FeignClient("service-hello")
public interface SchedualServiceHello {
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}