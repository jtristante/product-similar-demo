package com.example.products.service;

import com.example.products.client.ProductClient;
import com.example.products.model.ProductDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("java17")
class Java17ProductServiceTest {

	@Autowired
	private ProductService productService;

	@MockitoBean
	private ProductClient productClient;

	@Test
	void getSimilarProducts_WithMultipleSimilarIds_ReturnsProductDetails() {
		// Given
		String productId = "1";
		List<String> similarIds = Arrays.asList("2", "3", "4");
		ProductDetail product2 = new ProductDetail("2", "Product 2", 29.99, true);
		ProductDetail product3 = new ProductDetail("3", "Product 3", 39.99, false);
		ProductDetail product4 = new ProductDetail("4", "Product 4", 49.99, true);

		when(productClient.getSimilarProductIds(productId)).thenReturn(similarIds);
		when(productClient.getProductDetail("2")).thenReturn(product2);
		when(productClient.getProductDetail("3")).thenReturn(product3);
		when(productClient.getProductDetail("4")).thenReturn(product4);

		// When
		List<ProductDetail> result = productService.getSimilarProducts(productId);

		// Then
		assertThat(result).hasSize(3);
		assertThat(result).extracting(ProductDetail::id).containsExactly("2", "3", "4");
	}

	@Test
	void getSimilarProducts_WithNoSimilarIds_ReturnsEmptyList() {
		// Given
		String productId = "1";
		when(productClient.getSimilarProductIds(productId)).thenReturn(Collections.emptyList());

		// When
		List<ProductDetail> result = productService.getSimilarProducts(productId);

		// Then
		assertThat(result).isEmpty();
	}

	@Test
	void getSimilarProducts_WithPartialFailures_ReturnsSuccessfulResults() {
		// Given
		String productId = "1";
		List<String> similarIds = Arrays.asList("2", "3");
		ProductDetail product2 = new ProductDetail("2", "Product 2", 29.99, true);

		when(productClient.getSimilarProductIds(productId)).thenReturn(similarIds);
		when(productClient.getProductDetail("2")).thenReturn(product2);
		when(productClient.getProductDetail("3")).thenReturn(null);

		// When
		List<ProductDetail> result = productService.getSimilarProducts(productId);

		// Then
		assertThat(result).hasSize(1);
		assertThat(result.getFirst().id()).isEqualTo("2");
	}
}
