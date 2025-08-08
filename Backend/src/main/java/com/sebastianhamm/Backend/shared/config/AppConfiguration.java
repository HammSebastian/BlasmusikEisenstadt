package com.sebastianhamm.Backend.shared.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Configuration properties for application-specific settings.
 * Maps properties with prefix 'app.*' to typed configuration.
 */
@ConfigurationProperties(prefix = "app")
@Validated
public record AppConfiguration(
        
        /**
         * CORS configuration settings.
         */
        @Valid
        Cors cors,
        
        /**
         * Environment setting (e.g., production, development).
         */
        String environment,
        
        /**
         * Debug configuration.
         */
        @Valid
        Debug debug,
        
        /**
         * Rate limiting configuration.
         */
        @Valid
        RateLimit rateLimit,
        
        /**
         * File upload configuration.
         */
        @Valid
        Upload upload
) {
    
    /**
     * CORS configuration properties.
     */
    public record Cors(
            /**
             * List of allowed origins for CORS requests.
             */
            List<String> allowedOrigins,
            
            /**
             * Allowed HTTP methods for CORS requests.
             */
            List<String> allowedMethods,
            
            /**
             * Allowed headers for CORS requests.
             */
            List<String> allowedHeaders,
            
            /**
             * Whether credentials are allowed in CORS requests.
             */
            Boolean allowCredentials,
            
            /**
             * Maximum age for CORS preflight requests in seconds.
             */
            Integer maxAge
    ) {}
    
    /**
     * Debug configuration properties.
     */
    public record Debug(
            /**
             * Whether debug mode is enabled.
             */
            Boolean enabled
    ) {}
    
    /**
     * Rate limiting configuration properties.
     */
    public record RateLimit(
            /**
             * Whether rate limiting is enabled.
             */
            Boolean enabled,
            
            /**
             * Number of requests allowed per minute.
             */
            Integer requestsPerMinute
    ) {}
    
    /**
     * File upload configuration properties.
     */
    public record Upload(
            /**
             * Maximum file size for uploads.
             */
            String maxFileSize,
            
            /**
             * List of allowed file types for uploads.
             */
            List<String> allowedTypes,
            
            /**
             * Path where uploaded files are stored.
             */
            String path
    ) {}
}