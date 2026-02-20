package com.assessment.product_management_api.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}

