package com.example.products.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

	public static final String SIMILAR_IDS_CACHE = "similarIdsCache";
	public static final String PRODUCT_DETAIL_CACHE = "productDetailCache";

	@Primary
	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager();
		cacheManager.setCacheNames(Arrays.asList(SIMILAR_IDS_CACHE, PRODUCT_DETAIL_CACHE));
		cacheManager.setCaffeine(caffeineConfig());
		return cacheManager;
	}

	@Bean
	public Caffeine<Object, Object> caffeineConfig() {
		return Caffeine.newBuilder()
			.maximumSize(1000)
			.expireAfterWrite(5, TimeUnit.MINUTES)
			.recordStats();
	}
}
