package com.example.products.service;

import com.example.products.client.ProductClient;
import com.example.products.model.ProductDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@Profile("java17")
public class Java17ProductService implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(Java17ProductService.class);

	private final ProductClient productClient;
	private final Executor executor;

	public Java17ProductService(
			ProductClient productClient,
			@Qualifier("productServiceExecutor") Executor executor) {
		this.productClient = productClient;
		this.executor = executor;
	}

	@Override
	public List<ProductDetail> getSimilarProducts(String productId) {
		logger.info("Fetching similar products for productId: {}", productId);

		List<String> similarIds = productClient.getSimilarProductIds(productId);

		if (similarIds == null || similarIds.isEmpty()) {
			logger.info("No similar products found for productId: {}", productId);
			return Collections.emptyList();
		}

		logger.info("Found {} similar product IDs for productId: {}", similarIds.size(), productId);

		List<CompletableFuture<ProductDetail>> futures = similarIds.stream()
			.map(id -> CompletableFuture.supplyAsync(() -> {
				try {
					ProductDetail detail = productClient.getProductDetail(id);
					if (detail != null) {
						logger.debug("Successfully fetched product detail for id: {}", id);
					}
					return detail;
				} catch (Exception e) {
					logger.error("Failed to fetch product detail for id: {}", id, e);
					return null;
				}
			}, executor))
			.toList();

		CompletableFuture<Void> allFutures = CompletableFuture.allOf(
			futures.toArray(new CompletableFuture[0])
		);

		allFutures.join();

		List<ProductDetail> results = futures.stream()
			.map(CompletableFuture::join)
			.filter(Objects::nonNull)
			.toList();

		logger.info("Successfully fetched {} product details out of {} similar IDs for productId: {}",
			results.size(), similarIds.size(), productId);

		return results;
	}
}
