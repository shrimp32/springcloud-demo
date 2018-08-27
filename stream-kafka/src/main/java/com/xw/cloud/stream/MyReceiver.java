package com.xw.cloud.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.handler.annotation.SendTo;

/**
 * @author : 夏玮
 * Created on 2018/8/23 上午11:13
 */
@EnableBinding({MySink.class,Sink.class})
public class MyReceiver {

    private final Logger logger = LoggerFactory.getLogger(MyReceiver.class);

    @StreamListener(MySink.INPUT)
    @SendTo(Sink.INPUT)  //定义回执的通道
    private String receive(String msg) {
        logger.info(MySink.INPUT+" receive message : " + msg);
        return "receipt msg :"+ msg;
    }

    //接收回执信息
    @StreamListener(Sink.INPUT)
    private void receive3(String msg) {
        logger.info(Sink.INPUT + " receive message : " + msg);
    }


//    @StreamListener(Source.OUTPUT)
//    private void receive1(String msg) {
//        logger.info(Source.OUTPUT + " receive message : " + msg);
//    }

//    @StreamListener(MySource.OUTPUT)
//    private void receive2(String msg) {
//        logger.info(MySource.OUTPUT + " receive message : " + msg);
//    }
}
