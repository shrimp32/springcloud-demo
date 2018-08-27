package com.xw.cloud.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author : 夏玮
 * Created on 2018/8/17 下午4:44
 * 自定义的Sink
 */
public interface MySink {
    String INPUT = "mychannel";

    @Input(INPUT)
    SubscribableChannel input();
}
