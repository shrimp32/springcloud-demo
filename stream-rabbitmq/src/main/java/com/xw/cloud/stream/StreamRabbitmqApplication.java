package com.xw.cloud.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
@EnableBinding(Source.class)
public class StreamRabbitmqApplication implements CommandLineRunner{
	private static Logger logger = LoggerFactory.getLogger(StreamRabbitmqApplication.class);

	@Autowired
	SinkSender sinkSender;

	@Override
	public void run(String... strings) throws Exception {
		// 字符串类型发送MQ
		logger.info("字符串信息发送");
		sinkSender.output1().send(MessageBuilder.withPayload("大家好,output1").build());
		sinkSender.output2().send(MessageBuilder.withPayload("大家好,output2").build());
		sinkSender.output3().send(MessageBuilder.withPayload("大家好,output3").build());
	}

	public static void main(String[] args) {
		SpringApplication.run(StreamRabbitmqApplication.class, args);
	}
}
