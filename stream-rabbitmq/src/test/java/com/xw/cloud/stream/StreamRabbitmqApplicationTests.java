package com.xw.cloud.stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.xml.transform.Source;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
//@EnableBinding({SinkSender.class, Source.class})
public class StreamRabbitmqApplicationTests {
	@Autowired
	private SinkSender sinkSender;

	@Test
	public void contextLoads() {
//		sinkSender.output1().send(MessageBuilder.withPayload("hello 123").build());
//		sinkSender.output2().send(MessageBuilder.withPayload("hello 456").build());
	}

}
