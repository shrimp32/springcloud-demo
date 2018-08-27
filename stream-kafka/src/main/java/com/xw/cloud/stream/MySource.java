package com.xw.cloud.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;

/**
 * @author : 夏玮
 * Created on 2018/8/23 下午1:19
 * 自定义的输出通道，发送端
 */
public interface MySource {

    String OUTPUT = "mysource";

    @Output(MySource.OUTPUT)
    MessageChannel output1();

}
