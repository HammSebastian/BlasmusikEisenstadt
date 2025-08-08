package com.sebastianhamm.Backend.shared.config;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple unit test to verify configuration record structure without Spring context.
 */
class ConfigurationRecordsTest {

    @Test
    void auth0ConfigurationShouldCreateSuccessfully() {
        // Given
        String domain = "https://test.auth0.com/";
        String audience = "https://api.test.com/";
        String issuerUri = "https://test.auth0.com/";

        // When
        Auth0Configuration config = new Auth0Configuration(domain, audience, issuerUri);

        // Then
        assertNotNull(config);
        assertEquals(domain, config.domain());
        assertEquals(audience, config.audience());
        assertEquals(issuerUri, config.issuerUri());
    }

    @Test
    void appConfigurationShouldCreateSuccessfully() {
        // Given
        AppConfiguration.Cors cors = new AppConfiguration.Cors(
                List.of("http://localhost:3000"),
                List.of("GET", "POST"),
                List.of("*"),
                true,
                3600
        );
        AppConfiguration.Debug debug = new AppConfiguration.Debug(true);
        AppConfiguration.RateLimit rateLimit = new AppConfiguration.RateLimit(false, 100);
        AppConfiguration.Upload upload = new AppConfiguration.Upload(
                "10MB",
                List.of("image/jpeg"),
                "/tmp/test"
        );

        // When
        AppConfiguration config = new AppConfiguration(cors, "test", debug, rateLimit, upload);

        // Then
        assertNotNull(config);
        assertEquals("test", config.environment());
        assertNotNull(config.cors());
        assertEquals(List.of("http://localhost:3000"), config.cors().allowedOrigins());
        assertTrue(config.debug().enabled());
        assertFalse(config.rateLimit().enabled());
        assertEquals("10MB", config.upload().maxFileSize());
    }
}