/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.configuration;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.security.AudienceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    @Value("${auth0.domain}")
    private String auth0Domain;

    @Value("${auth0.audience}")
    private String auth0Audience;

    private static final String[] PUBLIC_MATCHERS = {
            "/about/**",
            "/events/**",
            "/gallery/**",
            "/history/**",
            "/images/**",
            "/locations/**",
            "/member/**",
            "/news/**",
            "/section/**",
            "/welcome/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, PUBLIC_MATCHERS).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS).hasAnyAuthority("write:admin", "write:reporter")
                        .requestMatchers(HttpMethod.PUT, PUBLIC_MATCHERS).hasAnyAuthority("write:admin", "write:reporter")
                        .requestMatchers(HttpMethod.DELETE, PUBLIC_MATCHERS).hasAnyAuthority("write:admin", "write:reporter")

                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwtDecoder()));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String issuerUri = auth0Domain;

        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> withIssuer = new JwtIssuerValidator(issuerUri);
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(auth0Audience);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("permissions");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200", "https://stadtkapelle-eisenstadt.at")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "https://stadtkapelle-eisenstadt.at"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
