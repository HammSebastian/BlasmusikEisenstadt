/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Global exception handler for security-related exceptions.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.security;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for security-related exceptions.
 * Provides consistent error responses for security issues like authentication failures,
 * authorization failures, and invalid tokens.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class SecurityExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handles access denied exceptions.
     *
     * @param ex The AccessDeniedException
     * @return A ResponseEntity with an error message
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Access denied: You don't have permission to access this resource")
                .statusCode(HttpStatus.FORBIDDEN.value())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles authentication exceptions.
     *
     * @param ex The AuthenticationException
     * @return A ResponseEntity with an error message
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Authentication failed: " + ex.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles bad credentials exceptions.
     *
     * @param ex The BadCredentialsException
     * @return A ResponseEntity with an error message
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        log.warn("Bad credentials: {}", ex.getMessage());
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Invalid credentials")
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles invalid bearer token exceptions.
     *
     * @param ex The InvalidBearerTokenException
     * @return A ResponseEntity with an error message
     */
    @ExceptionHandler(InvalidBearerTokenException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidBearerTokenException(InvalidBearerTokenException ex) {
        log.warn("Invalid bearer token: {}", ex.getMessage());
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Invalid or expired token")
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles OAuth2 authentication exceptions.
     *
     * @param ex The OAuth2AuthenticationException
     * @return A ResponseEntity with an error message
     */
    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleOAuth2AuthenticationException(OAuth2AuthenticationException ex) {
        log.warn("OAuth2 authentication error: {}", ex.getMessage());
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("OAuth2 authentication error: " + ex.getError().getDescription())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}