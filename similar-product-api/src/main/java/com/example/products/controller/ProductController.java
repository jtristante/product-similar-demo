package com.example.products.controller;

import com.example.products.client.ProductClient;
import com.example.products.model.ProductDetail;
import com.example.products.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	private final ProductService productService;
	private final ProductClient productClient;

	public ProductController(ProductService productService, ProductClient productClient) {
		this.productService = productService;
		this.productClient = productClient;
	}

	@GetMapping("/{productId}/similar")
	public ResponseEntity<List<ProductDetail>> getSimilarProducts(@PathVariable String productId) {
		logger.info("Received request for similar products: productId={}", productId);

		ProductDetail product = productClient.getProductDetail(productId);
		if (product == null) {
			logger.warn("Product not found: {}", productId);
			return ResponseEntity.notFound().build();
		}

		List<ProductDetail> similarProducts = productService.getSimilarProducts(productId);
		logger.info("Returning {} similar products for productId: {}", similarProducts.size(), productId);
		return ResponseEntity.ok(similarProducts);
	}
}
