package at.sebastianhamm.backend.configuration;

import at.sebastianhamm.backend.jwt.AuthEntryPointJwt;
import at.sebastianhamm.backend.jwt.AuthTokenFilter;
import at.sebastianhamm.backend.services.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

/**
 * Security configuration for the application.
 * Configures authentication, authorization, JWT filters and CORS for production use.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final Environment env;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * BCrypt with strength 12 for production-grade password hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Configures HTTP security, JWT filters and authorization rules.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // JWT is stateless, CSRF unnecessary
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                            Map<String, Object> body = new HashMap<>();
                            body.put("status", HttpServletResponse.SC_FORBIDDEN);
                            body.put("error", "Forbidden");
                            body.put("message", accessDeniedException.getMessage());
                            body.put("path", request.getServletPath());

                            new ObjectMapper().writeValue(response.getOutputStream(), body);
                        }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/public/hero-items").permitAll()
                        .requestMatchers(HttpMethod.GET, "/public/announcements").permitAll()
                        .requestMatchers(HttpMethod.GET, "/public/gigs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/public/gigs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/public/members").permitAll()
                        .requestMatchers(HttpMethod.GET, "/public/members/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/public/about").permitAll()

                        .requestMatchers(HttpMethod.GET, "/public/announcements/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/public/announcements/**").hasAnyRole("ADMIN", "REPORTER")
                        .requestMatchers(HttpMethod.PUT, "/public/announcements/**").hasAnyRole("ADMIN", "REPORTER")
                        .requestMatchers(HttpMethod.DELETE, "/public/announcements/**").hasAnyRole("ADMIN", "REPORTER")

                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/profile").authenticated()
                        .anyRequest().authenticated()
                );


        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS configuration for frontend communication. Allows only trusted origins.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        String[] allowedOrigins = {
                "https://stadtkapelle-eisenstadt.at",
                "http://localhost:4200"
        };

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}