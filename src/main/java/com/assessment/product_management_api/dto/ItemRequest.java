package com.assessment.product_management_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemRequest(
        @NotNull
        @Min(1)
        Integer quantity
) {
}

