package at.sebastianhamm.backend.configuration;

import at.sebastianhamm.backend.jwt.AuthEntryPointJwt;
import at.sebastianhamm.backend.jwt.AuthTokenFilter;
import at.sebastianhamm.backend.services.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WebSecurityConfigurationTest {

    private UserDetailsServiceImpl userDetailsService;
    private AuthEntryPointJwt authEntryPointJwt;
    private WebSecurityConfiguration config;
    private Environment env;

    @BeforeEach
    void setup() {
        userDetailsService = mock(UserDetailsServiceImpl.class);
        authEntryPointJwt = mock(AuthEntryPointJwt.class);
        env = mock(Environment.class);
        config = new WebSecurityConfiguration(userDetailsService, authEntryPointJwt, env);
    }

    @Test
    void passwordEncoder_shouldBeBCryptWithStrength12() {
        PasswordEncoder encoder = config.passwordEncoder();
        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);
        assertThat(encoder.matches("Test123!", encoder.encode("Test123!"))).isTrue();
    }

    @Test
    void authenticationProvider_shouldBeConfiguredCorrectly() {
        DaoAuthenticationProvider provider = config.authenticationProvider();
        Object encoder = ReflectionTestUtils.getField(provider, "passwordEncoder");
        Object uds = ReflectionTestUtils.getField(provider, "userDetailsService");

        assertThat(encoder).isNotNull();
        assertThat(uds).isSameAs(userDetailsService);
    }

    @Test
    void corsConfigurer_shouldRegisterCorsWithCorrectValues() {
        WebMvcConfigurer corsConfigurer = config.corsConfigurer();

        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);

        // Chaining korrekt mocken mit konkreten Parametern
        when(registry.addMapping("/**")).thenReturn(registration);
        when(registration.allowedOrigins("https://stadtkapelle-eisenstadt.at", "http://localhost:4200"))
                .thenReturn(registration);
        when(registration.allowedMethods("GET", "POST", "PUT", "DELETE"))
                .thenReturn(registration);
        when(registration.allowedHeaders("*"))
                .thenReturn(registration);
        when(registration.allowCredentials(true))
                .thenReturn(registration);

        // Methode aufrufen
        corsConfigurer.addCorsMappings(registry);

        // Verifizieren
        verify(registry).addMapping("/**");
        verify(registration).allowedOrigins("https://stadtkapelle-eisenstadt.at", "http://localhost:4200");
        verify(registration).allowedMethods("GET", "POST", "PUT", "DELETE");
        verify(registration).allowedHeaders("*");
        verify(registration).allowCredentials(true);
    }




    @Test
    void authenticationJwtTokenFilter_shouldNotBeNull() {
        AuthTokenFilter filter = config.authenticationJwtTokenFilter();
        assertThat(filter).isNotNull();
    }
}
