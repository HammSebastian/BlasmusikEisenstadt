package com.sebastianhamm.Backend.shared.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for Auth0 authentication.
 * Maps properties with prefix 'auth0.*' to typed configuration.
 */
@ConfigurationProperties(prefix = "auth0")
@Validated
public record Auth0Configuration(
        
        /**
         * The domain of your Auth0 tenant.
         * Must be a valid HTTPS domain URL without trailing path.
         * Example: https://dev-xyz.eu.auth0.com/
         */
        @NotBlank(message = "Auth0 domain must not be empty")
        @Pattern(regexp = "^https://[^/]+/?$", message = "Invalid auth0.domain property. Must be a valid HTTPS domain URL without trailing path.")
        String domain,
        
        /**
         * The identifier (audience) of your API that you created in Auth0.
         * This is the value you set as 'https://api.stadtkapelle-eisenstadt.at/'.
         * This MUST match exactly!
         */
        @NotBlank(message = "Auth0 audience must not be empty")
        String audience,
        
        /**
         * The issuer URI for Auth0 JWT validation.
         * Used for OAuth2 resource server configuration.
         */
        @NotBlank(message = "Auth0 issuer URI must not be empty")
        String issuerUri
) {
}