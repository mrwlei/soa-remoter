package com.soa.provider;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;

public class TestSoaProviderApplication {

	public static void main(String[] args) throws InterruptedException {

		CountDownLatch countDownLatch = new CountDownLatch(1);
		ApplicationContext app = new ClassPathXmlApplicationContext("mytest.xml");

		countDownLatch.await();

//		TestService testService = (TestService) app.getBean("remoteService");
//		testService.say("Hi");

	}

}

