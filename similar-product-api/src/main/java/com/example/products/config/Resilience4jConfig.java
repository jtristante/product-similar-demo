package com.example.products.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;

@Configuration
public class Resilience4jConfig {

	public static final String SIMULATOR_CIRCUIT_BREAKER = "simulator";
	public static final String SIMULATOR_RETRY = "simulatorRetry";
	public static final String SIMULATOR_TIME_LIMITER = "simulatorTimeLimiter";

	@Bean
	public CircuitBreakerRegistry circuitBreakerRegistry() {
		CircuitBreakerConfig config = CircuitBreakerConfig.custom()
			.failureRateThreshold(50)
			.waitDurationInOpenState(Duration.ofMillis(1000))
			.permittedNumberOfCallsInHalfOpenState(3)
			.slidingWindowSize(10)
			.build();

		return CircuitBreakerRegistry.of(config);
	}

	@Bean
	public TimeLimiterRegistry timeLimiterRegistry() {
		TimeLimiterConfig config = TimeLimiterConfig.custom()
			.timeoutDuration(Duration.ofSeconds(5))
			.cancelRunningFuture(true)
			.build();

		return TimeLimiterRegistry.of(config);
	}

	@Bean
	public RetryRegistry retryRegistry() {
		RetryConfig config = RetryConfig.custom()
			.maxAttempts(3)
			.waitDuration(Duration.ofMillis(500))
			.retryExceptions(Throwable.class)
			.build();

		return RetryRegistry.of(config);
	}
}
