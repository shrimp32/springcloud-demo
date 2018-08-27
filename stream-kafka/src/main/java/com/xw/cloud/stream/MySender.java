package com.xw.cloud.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;

/**
 * @author : 夏玮
 * Created on 2018/8/23 上午10:59
 */

@EnableBinding({MySource.class,Source.class})
public class MySender {

    private static final Logger logger = LoggerFactory.getLogger(MySender.class);

    @Autowired
    private MySource mySource;
    public void sendMessage(String message) {
        try {
            logger.info(mySource.OUTPUT+" send message:"+message);
            mySource.output1().send(MessageBuilder.withPayload(message).build());
        } catch (Exception e) {
            logger.info("消息发送失败，原因：" + e);
            e.printStackTrace();
        }
    }

    @Autowired
    private Source source;
    public void sendMessage1(String message) {
        try {
            logger.info(source.OUTPUT+" send message:"+message);
            source.output().send(MessageBuilder.withPayload(message).build());
        } catch (Exception e) {
            logger.info("消息发送失败，原因：" + e);
            e.printStackTrace();
        }
    }
}

