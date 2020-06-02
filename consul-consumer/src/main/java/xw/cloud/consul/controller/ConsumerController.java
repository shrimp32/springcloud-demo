package xw.cloud.consul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xw.cloud.consul.service.ConsumerService;

/**
 * @author : 夏玮
 * Created on 2019-01-28 15:06
 * 消费者
 */
@RestController
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @GetMapping("/consumer")
    public String getProducer(){

        return consumerService.consumer();
    }
}
