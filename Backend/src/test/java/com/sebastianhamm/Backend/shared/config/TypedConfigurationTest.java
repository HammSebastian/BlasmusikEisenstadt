package com.sebastianhamm.Backend.shared.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Test to verify that typed configuration classes can be loaded properly.
 */
@SpringBootTest(classes = {Auth0Configuration.class, AppConfiguration.class})
@EnableConfigurationProperties({Auth0Configuration.class, AppConfiguration.class})
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "auth0.domain=https://test.auth0.com/",
    "auth0.audience=https://api.test.com/",
    "auth0.issuer-uri=https://test.auth0.com/",
    "app.cors.allowed-origins=http://localhost:3000",
    "app.environment=test",
    "app.debug.enabled=true",
    "app.rate-limit.enabled=false",
    "app.rate-limit.requests-per-minute=100",
    "app.upload.max-file-size=10MB",
    "app.upload.allowed-types=image/jpeg,image/png",
    "app.upload.path=/tmp/test"
})
class TypedConfigurationTest {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> {
            // Test passes if Spring context loads successfully with typed configuration
        });
    }
}