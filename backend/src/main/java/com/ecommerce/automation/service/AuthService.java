package com.ecommerce.automation.service;

import com.ecommerce.automation.dto.LoginRequest;
import com.ecommerce.automation.dto.LoginResponse;
import com.ecommerce.automation.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private static final String DEFAULT_EMAIL = "seller@automation.com";
    private static final String DEFAULT_PASSWORD = "Seller@123";

    private final Set<String> activeTokens = ConcurrentHashMap.newKeySet();

    public LoginResponse login(LoginRequest request) {
        log.info("Login requested by email: {}", request.email());
        if (!DEFAULT_EMAIL.equalsIgnoreCase(request.email()) || !DEFAULT_PASSWORD.equals(request.password())) {
            log.warn("Invalid login attempt for email: {}", request.email());
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        String token = UUID.randomUUID().toString();
        activeTokens.add(token);
        log.info("Login successful for email: {}", request.email());
        return new LoginResponse(token, request.email(), "Login successful");
    }

    public void validateToken(String token) {
        if (token == null || token.isBlank() || !activeTokens.contains(token)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Unauthorized user. Please login again.");
        }
    }
}
