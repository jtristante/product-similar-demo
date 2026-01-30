package com.example.products.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("java25")
public class Java25Configuration {

	@Bean(name = "productServiceExecutor")
	public Executor productServiceExecutor() {
		ThreadFactory factory = Thread.ofVirtual()
			.name("product-service-vt-", 0)
			.factory();
		return Executors.newThreadPerTaskExecutor(factory);
	}
}
