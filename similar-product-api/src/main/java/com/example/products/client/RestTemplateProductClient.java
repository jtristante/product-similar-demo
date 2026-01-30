package com.example.products.client;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.products.model.ProductDetail;

@Component
public class RestTemplateProductClient implements ProductClient {

	private static final Logger logger = LoggerFactory.getLogger(RestTemplateProductClient.class);

	private final RestTemplate restTemplate;
	private final String simulatorBaseUrl;

	public RestTemplateProductClient(
			RestTemplate restTemplate,
			@Value("${simulator.base-url}") String simulatorBaseUrl) {
		this.restTemplate = restTemplate;
		this.simulatorBaseUrl = simulatorBaseUrl;
	}

	@Override
	public List<String> getSimilarProductIds(String productId) {
		try {
			String url = simulatorBaseUrl + "/product/{productId}/similarids";
			ResponseEntity<List<String>> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {},
				productId
			);
			return response.getBody();
		} catch (HttpClientErrorException.NotFound _) {
			logger.warn("Product not found for similar IDs lookup: {}", productId);
			return Collections.emptyList();
		} catch (Exception e) {
			logger.error("Error fetching similar product IDs for product: {}", productId, e);
			return Collections.emptyList();
		}
	}

	@Override
	public ProductDetail getProductDetail(String productId) {
		try {
			String url = simulatorBaseUrl + "/product/{productId}";
			return restTemplate.getForObject(url, ProductDetail.class, productId);
		} catch (HttpClientErrorException.NotFound _) {
			logger.warn("Product not found: {}", productId);
			return null;
		} catch (Exception e) {
			logger.error("Error fetching product detail for product: {}", productId, e);
			return null;
		}
	}
}
