package com.assessment.product_management_api.dto;

import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String productName,
        String createdBy,
        LocalDateTime createdOn,
        String modifiedBy,
        LocalDateTime modifiedOn
) {
}

