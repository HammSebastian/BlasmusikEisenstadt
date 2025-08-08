package com.sebastianhamm.Backend;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.TestPropertySource;

/**
 * Base class for integration tests.
 * Provides common setup for Spring Boot integration tests including:
 * - Testcontainers configuration
 * - MockMvc setup with security
 * - Test profiles and properties
 * - Random port configuration
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false",
    "logging.level.org.springframework.security=DEBUG",
    "logging.level.org.testcontainers=INFO"
})
public abstract class BaseIT {

    @LocalServerPort
    protected int port;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    /**
     * Sets up MockMvc with security configuration before each test.
     * This ensures that security filters are properly configured for testing.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    /**
     * Helper method to get the base URL for the test server.
     *
     * @return Base URL with random port
     */
    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    /**
     * Helper method to get API base URL.
     *
     * @return API base URL with random port
     */
    protected String getApiBaseUrl() {
        return getBaseUrl() + "/api/v1";
    }
}