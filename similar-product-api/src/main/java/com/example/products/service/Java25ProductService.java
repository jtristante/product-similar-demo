package com.example.products.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.StructuredTaskScope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.example.products.client.ProductClient;
import com.example.products.model.ProductDetail;

@Service
@Profile("java25")
public class Java25ProductService implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(Java25ProductService.class);

	private final ProductClient productClient;

	public Java25ProductService(ProductClient productClient) {
		this.productClient = productClient;
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

		try (var scope = StructuredTaskScope.<ProductDetail>open()) {
			List<StructuredTaskScope.Subtask<ProductDetail>> subtasks = new ArrayList<>();

			for (String id : similarIds) {
				StructuredTaskScope.Subtask<ProductDetail> subtask = scope.fork(() -> {
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
				});
				subtasks.add(subtask);
			}

			scope.join();

			List<ProductDetail> results = subtasks.stream()
				.map(StructuredTaskScope.Subtask::get)
				.filter(Objects::nonNull)
				.toList();

			logger.info("Successfully fetched {} product details out of {} similar IDs for productId: {}",
				results.size(), similarIds.size(), productId);

			return results;
		} catch (InterruptedException _) {
			Thread.currentThread().interrupt();
			logger.error("Thread interrupted in structured concurrency scope for productId: {}", productId);
			return Collections.emptyList();
		} catch (Exception e) {
			logger.error("Error in structured concurrency scope for productId: {}", productId, e);
			return Collections.emptyList();
		}
	}
}
