package com.sebastianhamm.Backend.shared.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;

/**
 * Configuration for custom health checks to monitor application dependencies.
 * Provides detailed health information for database and other critical components.
 */
@Configuration
public class HealthCheckConfiguration {

    /**
     * Custom database health indicator with enhanced connection testing.
     */
    @Bean
    public HealthIndicator databaseHealthIndicator(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        return () -> {
            try {
                Instant start = Instant.now();
                
                // Test database connection
                try (Connection connection = dataSource.getConnection()) {
                    if (connection.isValid(5)) { // 5 second timeout
                        // Test query execution
                        jdbcTemplate.queryForObject("SELECT 1", Integer.class);
                        
                        Duration responseTime = Duration.between(start, Instant.now());
                        
                        return Health.up()
                                .withDetail("database", "PostgreSQL")
                                .withDetail("responseTime", responseTime.toMillis() + "ms")
                                .withDetail("status", "Connection successful")
                                .withDetail("timestamp", Instant.now().toString())
                                .build();
                    } else {
                        return Health.down()
                                .withDetail("database", "PostgreSQL")
                                .withDetail("error", "Connection validation failed")
                                .withDetail("timestamp", Instant.now().toString())
                                .build();
                    }
                }
            } catch (SQLException e) {
                return Health.down()
                        .withDetail("database", "PostgreSQL")
                        .withDetail("error", e.getMessage())
                        .withDetail("timestamp", Instant.now().toString())
                        .build();
            } catch (Exception e) {
                return Health.down()
                        .withDetail("database", "PostgreSQL")
                        .withDetail("error", "Unexpected error: " + e.getMessage())
                        .withDetail("timestamp", Instant.now().toString())
                        .build();
            }
        };
    }

    /**
     * Auth0 connectivity health indicator.
     */
    @Bean
    public HealthIndicator auth0HealthIndicator(Auth0Configuration auth0Config) {
        return () -> {
            try {
                // Simple validation of Auth0 configuration
                if (auth0Config.domain() == null || auth0Config.domain().isBlank()) {
                    return Health.down()
                            .withDetail("auth0", "Configuration")
                            .withDetail("error", "Auth0 domain not configured")
                            .withDetail("timestamp", Instant.now().toString())
                            .build();
                }
                
                if (auth0Config.audience() == null || auth0Config.audience().isBlank()) {
                    return Health.down()
                            .withDetail("auth0", "Configuration")
                            .withDetail("error", "Auth0 audience not configured")
                            .withDetail("timestamp", Instant.now().toString())
                            .build();
                }
                
                if (auth0Config.issuerUri() == null || auth0Config.issuerUri().isBlank()) {
                    return Health.down()
                            .withDetail("auth0", "Configuration")
                            .withDetail("error", "Auth0 issuer URI not configured")
                            .withDetail("timestamp", Instant.now().toString())
                            .build();
                }
                
                return Health.up()
                        .withDetail("auth0", "Configuration")
                        .withDetail("domain", auth0Config.domain())
                        .withDetail("audience", auth0Config.audience())
                        .withDetail("issuerUri", auth0Config.issuerUri())
                        .withDetail("status", "Configuration valid")
                        .withDetail("timestamp", Instant.now().toString())
                        .build();
                        
            } catch (Exception e) {
                return Health.down()
                        .withDetail("auth0", "Configuration")
                        .withDetail("error", "Configuration check failed: " + e.getMessage())
                        .withDetail("timestamp", Instant.now().toString())
                        .build();
            }
        };
    }

    /**
     * Application configuration health indicator.
     */
    @Bean
    public HealthIndicator applicationConfigHealthIndicator(AppConfiguration appConfig) {
        return () -> {
            try {
                Health.Builder healthBuilder = Health.up();
                
                // Check CORS configuration
                if (appConfig.cors() == null || 
                    appConfig.cors().allowedOrigins() == null || 
                    appConfig.cors().allowedOrigins().isEmpty()) {
                    healthBuilder.status(Status.DOWN)
                            .withDetail("cors", "Not properly configured");
                } else {
                    healthBuilder.withDetail("cors", "Configured with " + 
                            appConfig.cors().allowedOrigins().size() + " allowed origins");
                }
                
                // Check environment setting
                if (appConfig.environment() == null || appConfig.environment().isBlank()) {
                    healthBuilder.withDetail("environment", "Not specified");
                } else {
                    healthBuilder.withDetail("environment", appConfig.environment());
                }
                
                // Check debug configuration
                if (appConfig.debug() != null) {
                    healthBuilder.withDetail("debug", appConfig.debug().enabled());
                }
                
                // Check rate limiting configuration
                if (appConfig.rateLimit() != null) {
                    healthBuilder.withDetail("rateLimit", 
                            "enabled=" + appConfig.rateLimit().enabled() + 
                            ", rpm=" + appConfig.rateLimit().requestsPerMinute());
                }
                
                return healthBuilder
                        .withDetail("timestamp", Instant.now().toString())
                        .build();
                        
            } catch (Exception e) {
                return Health.down()
                        .withDetail("application", "Configuration")
                        .withDetail("error", "Configuration check failed: " + e.getMessage())
                        .withDetail("timestamp", Instant.now().toString())
                        .build();
            }
        };
    }
}