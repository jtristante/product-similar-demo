package com.example.products.client;

import java.util.List;

import com.example.products.model.ProductDetail;

public interface ProductClient {

	List<String> getSimilarProductIds(String productId);

	ProductDetail getProductDetail(String productId);
}
