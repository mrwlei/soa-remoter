package com.soa.consumer;

import com.soa.consumer.service.TestService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//@SpringBootApplication
public class SoaConsumerApplication {

	public static void main(String[] args) {
//		SpringApplication.run(SoaConsumerApplication.class, args);


		ApplicationContext app = new ClassPathXmlApplicationContext("mytest.xml");

		TestService testService = (TestService) app.getBean(TestService.class);

		System.out.println(testService.saveOrder("123456"));
		System.out.println("aaa");
	}

}

