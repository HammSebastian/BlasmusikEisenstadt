/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * <p>
 * Security configuration for the application.
 * Configures authentication, authorization, CORS, and JWT handling.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.configuration;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.security.AudienceValidator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Security configuration for the application.
 * Configures authentication, authorization, CORS, and JWT handling.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfiguration {

    @Value("${auth0.domain}")
    private String auth0Domain;

    @Value("${auth0.audience}")
    private String auth0Audience;

    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Value("${AUTH0_ISSUER_URI}")
    private String issuerUri;

    @PostConstruct
    public void validateProperties() {
        if (auth0Domain == null || !auth0Domain.matches("^https://[^/]+/?$")) {
            throw new IllegalArgumentException("Invalid auth0.domain property. Must be a valid HTTPS domain URL without trailing path.");
        }
        if (auth0Audience == null || auth0Audience.isBlank()) {
            throw new IllegalArgumentException("auth0.audience must not be empty.");
        }
    }


    private static final String[] PUBLIC_GET_MATCHERS = {
            "/about/**",
            "/events/**",
            "/gallery/**",
            "/history/**",
            "/images/**",
            "/locations/**",
            "/member/**",
            "/news/**",
            "/section/**",
            "/welcome/**"
    };

    private static final String[] PUBLIC_ACTUATOR_ENDPOINTS = {
            "/actuator/health",
            "/actuator/info",
            "/actuator/metrics"
    };

    @Configuration
    // @Profile("dev")
    public static class SwaggerSecurityConfig {
        @Bean
        @Order(1)
        public SecurityFilterChain swaggerSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .securityMatcher("/swagger-ui/**", "/v3-docs/**", "/api/v1/swagger-ui/**", "/api/v1/v3-docs/**")
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()
                    );
            return http.build();
        }
    }

    /**
     * API Security Chain: Absicherung der API-Endpunkte.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable) // stateless REST API
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3-docs/**"
                        ).permitAll()
                        // Öffentliche GET-Endpoints
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_MATCHERS).permitAll()
                        // Admin- und Reporter-Rollen: spezifisch Pfad+Methode sichern
                        .requestMatchers(HttpMethod.POST, "/admin/**").hasAuthority("write:admin")
                        .requestMatchers(HttpMethod.PUT, "/admin/**").hasAuthority("write:admin")
                        .requestMatchers(HttpMethod.DELETE, "/admin/**").hasAuthority("write:admin")

                        .requestMatchers(HttpMethod.POST, "/reporter/**").hasAuthority("write:reporter")
                        .requestMatchers(HttpMethod.PUT, "/reporter/**").hasAuthority("write:reporter")
                        .requestMatchers(HttpMethod.DELETE, "/reporter/**").hasAuthority("write:reporter")

                        // Rest: authentifiziert, keine Rolle
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    /**
     * Actuator Security Chain.
     */
    @Bean
    @Order(3)
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/actuator/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ACTUATOR_ENDPOINTS).permitAll()
                        .requestMatchers("/actuator/**").hasAuthority("ACTUATOR_ADMIN")
                        .anyRequest().denyAll()
                );

        return http.build();
    }

    /**
     * JWT Decoder mit Auth0-Validatoren.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> withIssuer = new JwtIssuerValidator(issuerUri);
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(auth0Audience);

        OAuth2TokenValidator<Jwt> withExpiration = jwt -> {
            if (jwt.getExpiresAt() == null || jwt.getExpiresAt().isBefore(java.time.Instant.now())) {
                return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "The token is expired", null));
            }
            return OAuth2TokenValidatorResult.success();
        };

        OAuth2TokenValidator<Jwt> withNotBefore = jwt -> {
            if (jwt.getNotBefore() != null && jwt.getNotBefore().isAfter(java.time.Instant.now())) {
                return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "The token is not valid yet", null));
            }
            return OAuth2TokenValidatorResult.success();
        };

        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator, withExpiration, withNotBefore);

        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }


    /**
     * Konvertiert JWT permissions Claim zu GrantedAuthorities.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("permissions");
        converter.setAuthorityPrefix(""); // keine Prefix "SCOPE_" etc.

        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtAuthConverter;
    }

    /**
     * CORS Konfiguration aus Properties, dynamisch.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Explizite Prüfung und Filterung der Origins - keine Wildcards
        List<String> filteredOrigins = allowedOrigins.stream()
                .filter(origin -> origin != null && !origin.isBlank() && !origin.equals("*"))
                .map(String::trim)
                .toList();

        if (filteredOrigins.isEmpty()) {
            throw new IllegalStateException("No valid CORS allowed origins configured. Please configure non-empty list without '*'.");
        }

        corsConfig.setAllowedOrigins(filteredOrigins);
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        log.info("CORS allowed origins: {}", filteredOrigins);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

}
