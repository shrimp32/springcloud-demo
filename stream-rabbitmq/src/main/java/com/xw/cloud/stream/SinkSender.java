package com.xw.cloud.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.MessageChannel;

/**
 * @author : 夏玮
 * Created on 2018/8/17 下午3:45
 * 发送通道
 */

public interface SinkSender{
    @Output(MySink.INPUT)//自定义的通道，输出通道和输入通道相同
    MessageChannel output1();

    @Output(Sink.INPUT)
    MessageChannel output2();

    @Output("input3") // input3通道输出
    MessageChannel output3();

}
