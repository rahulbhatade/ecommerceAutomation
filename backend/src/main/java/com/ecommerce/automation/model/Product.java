package com.ecommerce.automation.model;

import java.math.BigDecimal;

public record Product(
        String id,
        String productName,
        BigDecimal price,
        BigDecimal discount
) {
}
