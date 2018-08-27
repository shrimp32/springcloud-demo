package com.xw.cloud.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;

/**
 * @author : 夏玮
 * Created on 2018/8/17 下午1:45
 */
@EnableBinding(value = {SinkSender.class,Source.class})//用来指定一个或多个定义了@Input 或者 @Output注解的接口，实现对消息通道的绑定
public class SinkReceiver {

    private static Logger logger = LoggerFactory.getLogger(SinkReceiver.class);

    @StreamListener(MySink.INPUT)
    @SendTo(Source.OUTPUT)  //定义回执发送的消息通道
    public String receipt1(Object playload) {
        logger.info("Received1:" + playload+",There is "+ MySink.INPUT);
        return "receipt msg :"+playload;
    }

    //接收回执
    @StreamListener(Source.OUTPUT)
    public void receive1(String msg) {
        logger.info("receipt1:"+msg);
    }

    @StreamListener(Sink.INPUT)
    public void receive2(Object playload) {
        logger.info("Received2:" + playload+",There is "+ Sink.INPUT);
    }

    @StreamListener("input3")
    public void receive3(Object playload) {
        logger.info("Received3:" + playload+",There is input3");
    }
}