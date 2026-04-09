package com.ecommerce.automation.dto;

import com.ecommerce.automation.model.Product;

import java.util.List;

public record UploadResult(
        String message,
        List<Product> products
) {
}
