package com.example.products.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDetail(
		@NotBlank
		String id,

		@NotBlank
		String name,

		@NotNull
		Double price,

		@NotNull
		boolean availability
) {
}
