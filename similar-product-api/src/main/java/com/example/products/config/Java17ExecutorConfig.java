package com.example.products.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@Profile("java17")
public class Java17ExecutorConfig {

	@Bean(name = "productServiceExecutor")
	public Executor productServiceExecutor(
			@Value("${executor.core-pool-size}") int corePoolSize,
			@Value("${executor.max-pool-size}") int maxPoolSize,
			@Value("${executor.queue-capacity}") int queueCapacity,
			@Value("${executor.keep-alive-seconds}") int keepAliveSeconds) {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setKeepAliveSeconds(keepAliveSeconds);
		executor.setThreadNamePrefix("product-service-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}
}
