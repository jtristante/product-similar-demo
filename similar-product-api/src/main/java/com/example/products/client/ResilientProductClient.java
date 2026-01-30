package com.example.products.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.example.products.config.Resilience4jConfig;
import com.example.products.model.ProductDetail;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@Component
@Primary
public class ResilientProductClient implements ProductClient {

	private final ProductClient productClient;

	public ResilientProductClient(@Qualifier("cachedProductClient") ProductClient productClient) {
		this.productClient = productClient;
	}

	@Override
	@CircuitBreaker(name = Resilience4jConfig.SIMULATOR_CIRCUIT_BREAKER)
	@Retry(name = Resilience4jConfig.SIMULATOR_RETRY)
	@TimeLimiter(name = Resilience4jConfig.SIMULATOR_TIME_LIMITER)
	public List<String> getSimilarProductIds(String productId) {
		return productClient.getSimilarProductIds(productId);
	}

	@Override
	@CircuitBreaker(name = Resilience4jConfig.SIMULATOR_CIRCUIT_BREAKER)
	@Retry(name = Resilience4jConfig.SIMULATOR_RETRY)
	@TimeLimiter(name = Resilience4jConfig.SIMULATOR_TIME_LIMITER)
	public ProductDetail getProductDetail(String productId) {
		return productClient.getProductDetail(productId);
	}
}
