package com.example.products.controller;

import com.example.products.client.ProductClient;
import com.example.products.model.ProductDetail;
import com.example.products.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductService productService;

	@MockitoBean
	private ProductClient productClient;

	@Test
	void getSimilarProducts_WhenProductExists_ReturnsSimilarProducts() throws Exception {
		// Given
		String productId = "1";
		ProductDetail product = new ProductDetail(productId, "Test Product", 99.99, true);
		ProductDetail similar1 = new ProductDetail("2", "Similar Product 1", 89.99, true);
		ProductDetail similar2 = new ProductDetail("3", "Similar Product 2", 79.99, false);
		List<ProductDetail> similarProducts = Arrays.asList(similar1, similar2);

		when(productClient.getProductDetail(productId)).thenReturn(product);
		when(productService.getSimilarProducts(productId)).thenReturn(similarProducts);

		// When & Then
		mockMvc.perform(get("/product/{productId}/similar", productId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].id").value("2"))
			.andExpect(jsonPath("$[0].name").value("Similar Product 1"))
			.andExpect(jsonPath("$[0].price").value(89.99))
			.andExpect(jsonPath("$[0].availability").value(true))
			.andExpect(jsonPath("$[1].id").value("3"))
			.andExpect(jsonPath("$[1].name").value("Similar Product 2"))
			.andExpect(jsonPath("$[1].price").value(79.99))
			.andExpect(jsonPath("$[1].availability").value(false));
	}

	@Test
	void getSimilarProducts_WhenProductNotFound_Returns404() throws Exception {
		// Given
		String productId = "999";
		when(productClient.getProductDetail(productId)).thenReturn(null);

		// When & Then
		mockMvc.perform(get("/product/{productId}/similar", productId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@Test
	void getSimilarProducts_WhenNoSimilarProducts_ReturnsEmptyList() throws Exception {
		// Given
		String productId = "1";
		ProductDetail product = new ProductDetail(productId, "Test Product", 99.99, true);

		when(productClient.getProductDetail(productId)).thenReturn(product);
		when(productService.getSimilarProducts(productId)).thenReturn(Collections.emptyList());

		// When & Then
		mockMvc.perform(get("/product/{productId}/similar", productId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$").isEmpty());
	}
}
