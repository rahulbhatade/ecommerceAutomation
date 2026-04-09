package com.ecommerce.automation.service;

import com.ecommerce.automation.dto.MarketplaceUploadResponse;
import com.ecommerce.automation.exception.ApiException;
import com.ecommerce.automation.model.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProductUploadService {
    private static final Logger log = LoggerFactory.getLogger(ProductUploadService.class);
    private static final DataFormatter DATA_FORMATTER = new DataFormatter();

    private final Map<String, List<Product>> cachedProductsByToken = new ConcurrentHashMap<>();

    public List<Product> parseAndCacheExcel(String token, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Please upload a valid Excel file.");
        }

        String name = file.getOriginalFilename() == null ? "unknown" : file.getOriginalFilename().toLowerCase();
        if (!name.endsWith(".xlsx")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only .xlsx files are supported.");
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            validateHeaders(sheet.getRow(0));

            List<Product> products = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                String id = readCellAsString(row.getCell(0));
                String productName = readCellAsString(row.getCell(1));
                BigDecimal price = readCellAsBigDecimal(row.getCell(2), "price", i + 1);
                BigDecimal discount = readCellAsBigDecimal(row.getCell(3), "discount", i + 1);

                if (id.isBlank() || productName.isBlank()) {
                    throw new ApiException(HttpStatus.BAD_REQUEST,
                            "id and product name are required at row " + (i + 1));
                }

                products.add(new Product(id, productName, price, discount));
            }

            cachedProductsByToken.put(token, products);
            log.info("Successfully parsed {} products for token {}", products.size(), token);
            return products;
        } catch (IOException e) {
            log.error("Failed to parse Excel file", e);
            throw new ApiException(HttpStatus.BAD_REQUEST, "Failed to read uploaded Excel file.");
        }
    }

    public MarketplaceUploadResponse uploadToMarketplace(String token, String marketplace) {
        List<Product> products = cachedProductsByToken.get(token);
        if (products == null || products.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "No products found. Please upload Excel first.");
        }

        String normalizedMarketplace = (marketplace == null || marketplace.isBlank())
                ? "Amazon"
                : marketplace.trim();

        log.info("Uploading {} products to {} for token {}", products.size(), normalizedMarketplace, token);

        return new MarketplaceUploadResponse(
                "Products uploaded to " + normalizedMarketplace + " successfully (simulated).",
                products.size(),
                normalizedMarketplace
        );
    }

    private void validateHeaders(Row headerRow) {
        if (headerRow == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Excel header row is missing.");
        }

        List<String> expected = List.of("id", "product name", "price", "discount");
        for (int i = 0; i < expected.size(); i++) {
            String actual = readCellAsString(headerRow.getCell(i)).toLowerCase();
            if (!expected.get(i).equals(actual)) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "Invalid header at column " + (i + 1) + ". Expected: " + expected.get(i));
            }
        }
    }

    private String readCellAsString(Cell cell) {
        return cell == null ? "" : DATA_FORMATTER.formatCellValue(cell).trim();
    }

    private BigDecimal readCellAsBigDecimal(Cell cell, String fieldName, int rowNumber) {
        String value = readCellAsString(cell);
        if (value.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, fieldName + " is required at row " + rowNumber);
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Invalid number for " + fieldName + " at row " + rowNumber + ".");
        }
    }
}
