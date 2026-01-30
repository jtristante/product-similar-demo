package com.example.products.service;

import java.util.List;

import com.example.products.model.ProductDetail;

public interface ProductService {

	List<ProductDetail> getSimilarProducts(String productId);
}
