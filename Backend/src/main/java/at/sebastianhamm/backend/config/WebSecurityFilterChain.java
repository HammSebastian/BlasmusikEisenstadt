package at.sebastianhamm.backend.config;

import at.sebastianhamm.backend.security.AuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityFilterChain {

    private final AuthFilter authFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(req->
                        req
                                .requestMatchers("/auth/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/locations/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/locations/**").hasAnyRole("ADMIN", "REPORTER")
                                .requestMatchers(HttpMethod.PUT, "/locations/**").hasAnyRole("ADMIN", "REPORTER")
                                .requestMatchers(HttpMethod.DELETE, "/locations/**").hasAnyRole("ADMIN", "REPORTER")

                                .requestMatchers(HttpMethod.GET, "/gigs/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/gigs/**").hasAnyRole("ADMIN", "REPORTER")
                                .requestMatchers(HttpMethod.PUT, "/gigs/**").hasAnyRole("ADMIN", "REPORTER")
                                .requestMatchers(HttpMethod.DELETE, "/gigs/**").hasAnyRole("ADMIN", "REPORTER")
                                .anyRequest().authenticated())
                .sessionManagement(mag-> mag.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
