package com.ecommerce.automation.controller;

import com.ecommerce.automation.dto.MarketplaceUploadResponse;
import com.ecommerce.automation.dto.UploadResult;
import com.ecommerce.automation.model.Product;
import com.ecommerce.automation.service.AuthService;
import com.ecommerce.automation.service.ProductUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final AuthService authService;
    private final ProductUploadService productUploadService;

    public ProductController(AuthService authService, ProductUploadService productUploadService) {
        this.authService = authService;
        this.productUploadService = productUploadService;
    }

    @PostMapping("/upload-excel")
    public UploadResult uploadExcel(
            @RequestHeader("X-AUTH-TOKEN") String token,
            @RequestParam("file") MultipartFile file
    ) {
        authService.validateToken(token);
        List<Product> products = productUploadService.parseAndCacheExcel(token, file);
        return new UploadResult("Excel parsed successfully", products);
    }

    @PostMapping("/upload-marketplace")
    public MarketplaceUploadResponse uploadToMarketplace(
            @RequestHeader("X-AUTH-TOKEN") String token,
            @RequestBody(required = false) Map<String, String> payload
    ) {
        authService.validateToken(token);
        String marketplace = payload == null ? "Amazon" : payload.getOrDefault("marketplace", "Amazon");
        return productUploadService.uploadToMarketplace(token, marketplace);
    }
}
