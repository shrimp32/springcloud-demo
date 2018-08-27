package com.xw.cloud;

import com.xw.cloud.stream.MySender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StreamKafkaApplication implements CommandLineRunner{
	private static Logger logger = LoggerFactory.getLogger(StreamKafkaApplication.class);

	@Autowired
	MySender sender;

	@Override
	public void run(String... strings) throws Exception {
		// 字符串类型发送MQ
		logger.info("字符串信息发送");
		sender.sendMessage("hello, 大家好.");
		sender.sendMessage1("hello, 大家好1.");
	}

	public static void main(String[] args) {
		SpringApplication.run(StreamKafkaApplication.class, args);
	}
}
