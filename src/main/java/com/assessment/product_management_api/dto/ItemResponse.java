package com.assessment.product_management_api.dto;

public record ItemResponse(
        Long id,
        Long productId,
        Integer quantity
) {
}

