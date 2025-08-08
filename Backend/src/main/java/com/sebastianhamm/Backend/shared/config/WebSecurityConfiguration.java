/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.shared.config;


import com.sebastianhamm.Backend.shared.security.AudienceValidator;
import com.sebastianhamm.Backend.shared.security.JwtAccessDeniedHandler;
import com.sebastianhamm.Backend.shared.security.JwtAuthenticationEntryPoint;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties({Auth0Configuration.class, AppConfiguration.class})
public class WebSecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    private final Auth0Configuration auth0Config;
    private final AppConfiguration appConfig;

    public WebSecurityConfiguration(JwtAuthenticationEntryPoint authenticationEntryPoint, 
                                  JwtAccessDeniedHandler accessDeniedHandler,
                                  Auth0Configuration auth0Config,
                                  AppConfiguration appConfig) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.auth0Config = auth0Config;
        this.appConfig = appConfig;
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

    @Bean
    @Order(1)
    public SecurityFilterChain swaggerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/v3-docs/**",
                        "/api/v1/swagger-ui/**",
                        "/api/v1/v3-docs/**"
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    /**
     * API Security Chain: Absicherung der API-Endpunkte mit Audit Logging und Security Headers.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable) // stateless REST API
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; " +
                                "script-src 'self' 'unsafe-inline'; " +
                                "style-src 'self' 'unsafe-inline'; " +
                                "img-src 'self' data: https:; " +
                                "font-src 'self'; " +
                                "connect-src 'self'; " +
                                "media-src 'self'; " +
                                "object-src 'none'; " +
                                "child-src 'none'; " +
                                "frame-ancestors 'none'; " +
                                "base-uri 'self'; " +
                                "form-action 'self';"
                        ))
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)
                                .preload(true))
                        .contentTypeOptions(Customizer.withDefaults())
                        .referrerPolicy(referrerPolicy -> referrerPolicy.policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                )
                .authorizeHttpRequests(auth -> auth
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
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
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
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(auth0Config.issuerUri());

        OAuth2TokenValidator<Jwt> withIssuer = new JwtIssuerValidator(auth0Config.issuerUri());
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(auth0Config.audience());

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
        converter.setAuthorityPrefix("");

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

        // Handle null CORS configuration gracefully
        List<String> allowedOrigins = (appConfig.cors() != null && appConfig.cors().allowedOrigins() != null) 
                ? appConfig.cors().allowedOrigins() 
                : List.of("http://localhost:3000", "http://localhost:8080");

        List<String> filteredOrigins = allowedOrigins.stream()
                .filter(origin -> origin != null && !origin.isBlank() && !origin.equals("*"))
                .map(String::trim)
                .toList();

        if (filteredOrigins.isEmpty()) {
            throw new IllegalStateException("No valid CORS allowed origins configured. Please configure non-empty list without '*'.");
        }

        corsConfig.setAllowedOrigins(filteredOrigins);
        
        // Use configured methods or secure defaults
        List<String> allowedMethods = (appConfig.cors() != null && appConfig.cors().allowedMethods() != null) 
                ? appConfig.cors().allowedMethods() 
                : List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
        corsConfig.setAllowedMethods(allowedMethods);
        
        // Use configured headers or secure defaults (no wildcards)
        List<String> allowedHeaders = (appConfig.cors() != null && appConfig.cors().allowedHeaders() != null) 
                ? appConfig.cors().allowedHeaders().stream()
                    .filter(header -> header != null && !header.equals("*"))
                    .toList()
                : List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "X-Correlation-ID");
        corsConfig.setAllowedHeaders(allowedHeaders);
        
        // Use configured credentials setting or secure default
        Boolean allowCredentials = (appConfig.cors() != null && appConfig.cors().allowCredentials() != null) 
                ? appConfig.cors().allowCredentials() 
                : false;
        corsConfig.setAllowCredentials(allowCredentials);
        
        // Use configured max age or secure default
        Integer maxAge = (appConfig.cors() != null && appConfig.cors().maxAge() != null) 
                ? appConfig.cors().maxAge() 
                : 3600;
        corsConfig.setMaxAge(maxAge.longValue());

        log.info("CORS Configuration - Origins: {}, Methods: {}, Headers: {}, Credentials: {}, MaxAge: {}", 
                filteredOrigins, allowedMethods, allowedHeaders, allowCredentials, maxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
