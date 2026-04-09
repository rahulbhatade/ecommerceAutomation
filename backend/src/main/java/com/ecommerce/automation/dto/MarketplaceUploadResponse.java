package com.ecommerce.automation.dto;

public record MarketplaceUploadResponse(
        String message,
        int totalUploaded,
        String targetMarketplace
) {
}
