package at.sebastianhamm.backend.security.jwt;

import at.sebastianhamm.backend.exception.JwtAuthenticationException;
import at.sebastianhamm.backend.security.JwtUtils;
import at.sebastianhamm.backend.security.SecurityConstants;
import at.sebastianhamm.backend.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> PERMITTED_PATHS = List.of(
            "/api/auth/", "/api/public/", "/swagger-ui/", "/v3/api-docs", "/actuator/health", "/actuator/info"
    );

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String JWT_COOKIE_NAME = "jwt";

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${app.jwt.cookie.secure:true}")
    private boolean secureCookie;

    @Value("${app.jwt.cookie.http-only:true}")
    private boolean httpOnlyCookie;

    @Value("${app.jwt.cookie.domain:}")
    private String cookieDomain;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return PERMITTED_PATHS.stream().anyMatch(path::startsWith)
                || path.matches(".*\\.(js|css|png|jpe?g|gif|ico|svg|woff2?|ttf|map)$");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null) {
                if (!jwtUtils.validateToken(jwt)) {
                    throw new JwtAuthenticationException("Invalid or blacklisted JWT token");
                }

                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (!jwtUtils.validateToken(jwt))
                        throw new JwtAuthenticationException("Invalid JWT token for user: " + username);

                    if (!userDetails.isAccountNonLocked()) throw new LockedException("Account locked");
                    if (!userDetails.isEnabled()) throw new DisabledException("Account disabled");
                    if (!userDetails.isCredentialsNonExpired()) throw new CredentialsExpiredException("Credentials expired");

                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.debug("Authenticated user: {}", username);
                }
            }
        } catch (JwtAuthenticationException | LockedException | DisabledException | CredentialsExpiredException | BadCredentialsException e) {
            log.warn("Authentication failed: {}", e.getMessage());
            request.setAttribute("jwt_error", e.getMessage());
        } catch (Exception e) {
            log.error("Error processing authentication: {}", e.getMessage(), e);
            request.setAttribute("jwt_error", "Authentication error: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(TOKEN_PREFIX)) {
            return headerAuth.substring(TOKEN_PREFIX.length());
        }
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(c -> JWT_COOKIE_NAME.equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        return null;
    }

    private void setTokenInResponse(HttpServletResponse response, String token) {
        response.setHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token);

        int maxAge = (int) TimeUnit.MILLISECONDS.toSeconds(SecurityConstants.JWT_EXPIRATION_MS);
        String cookie = String.format("%s=%s; Path=/; Max-Age=%d; %s%s; SameSite=%s",
                JWT_COOKIE_NAME,
                token,
                maxAge,
                secureCookie ? "Secure; " : "",
                httpOnlyCookie ? "HttpOnly; " : "",
                secureCookie ? "None" : "Lax");

        if (cookieDomain != null && !cookieDomain.isEmpty()) {
            cookie += "; Domain=" + cookieDomain;
        }
        response.setHeader("Set-Cookie", cookie);
    }
}
