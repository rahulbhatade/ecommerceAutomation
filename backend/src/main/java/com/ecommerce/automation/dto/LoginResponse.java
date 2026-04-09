package com.ecommerce.automation.dto;

public record LoginResponse(
        String token,
        String email,
        String message
) {
}
