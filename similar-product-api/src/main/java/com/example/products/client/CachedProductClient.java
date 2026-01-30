package com.example.products.client;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.example.products.config.CacheConfig;
import com.example.products.model.ProductDetail;

@Component
@Primary
public class CachedProductClient implements ProductClient {

	private final ProductClient productClient;

	public CachedProductClient(ProductClient productClient) {
		this.productClient = productClient;
	}

	@Override
	@Cacheable(value = CacheConfig.SIMILAR_IDS_CACHE, key = "#productId")
	public List<String> getSimilarProductIds(String productId) {
		return productClient.getSimilarProductIds(productId);
	}

	@Override
	@Cacheable(value = CacheConfig.PRODUCT_DETAIL_CACHE, key = "#productId")
	public ProductDetail getProductDetail(String productId) {
		return productClient.getProductDetail(productId);
	}
}
