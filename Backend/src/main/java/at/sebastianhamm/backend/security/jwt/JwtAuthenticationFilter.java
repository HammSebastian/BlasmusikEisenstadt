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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * JWT authentication filter that processes JWT tokens in the Authorization header or cookies.
 * Validates tokens and sets up the Spring Security context for authenticated users.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> PERMITTED_PATHS = Arrays.asList(
            "/api/auth/",
            "/api/public/",
            "/swagger-ui/",
            "/v3/api-docs",
            "/actuator/health",
            "/actuator/info"
    );

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String JWT_COOKIE_NAME = "jwt";
    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";

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
        return PERMITTED_PATHS.stream().anyMatch(path::startsWith) ||
               path.endsWith(".js") ||
               path.endsWith(".css") ||
               path.endsWith(".png") ||
               path.endsWith(".jpg") ||
               path.endsWith(".jpeg") ||
               path.endsWith(".gif") ||
               path.endsWith(".ico") ||
               path.endsWith(".svg") ||
               path.endsWith(".woff") ||
               path.endsWith(".woff2") ||
               path.endsWith(".ttf") ||
               path.endsWith(".map");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            
            if (jwt != null) {
                // Validate token structure and signature
                if (!jwtUtils.validateTokenStructure(jwt)) {
                    throw new JwtAuthenticationException("Invalid JWT token");
                }

                // Check if token is blacklisted (e.g., after logout)
                if (jwtUtils.isTokenBlacklisted(jwt)) {
                    throw new JwtAuthenticationException("Token has been blacklisted");
                }

                String username = jwtUtils.getUsernameFromToken(jwt);
                
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Load user details and validate token against user
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    
                    if (!jwtUtils.validateToken(jwt, userDetails)) {
                        throw new JwtAuthenticationException("Invalid JWT token for user: " + username);
                    }

                    // Check if account is locked
                    if (!userDetails.isAccountNonLocked()) {
                        throw new LockedException("User account is locked");
                    }

                    // Check if account is enabled
                    if (!userDetails.isEnabled()) {
                        throw new DisabledException("User account is disabled");
                    }

                    // Check if credentials are still valid
                    if (!userDetails.isCredentialsNonExpired()) {
                        throw new CredentialsExpiredException("Credentials have expired");
                    }

                    // Create authentication token and set it in the security context
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    // Refresh token if it's about to expire
                    if (jwtUtils.isTokenExpiringSoon(jwt)) {
                        String newToken = jwtUtils.refreshToken(jwt);
                        setTokenInResponse(response, newToken);
                        log.debug("Refreshed JWT token for user: {}", username);
                    }
                    
                    log.debug("Authenticated user: {}", username);
                }
            }
        } catch (JwtAuthenticationException | LockedException | DisabledException | 
                 CredentialsExpiredException | BadCredentialsException e) {
            log.warn("Authentication failed: {}", e.getMessage());
            request.setAttribute("jwt_error", e.getMessage());
        } catch (Exception e) {
            log.error("Error processing authentication: {}", e.getMessage(), e);
            request.setAttribute("jwt_error", "Error processing authentication: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Parses the JWT token from the request header or cookie.
     */
    private String parseJwt(HttpServletRequest request) {
        // 1. Try to get token from Authorization header
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(TOKEN_PREFIX)) {
            return headerAuth.substring(TOKEN_PREFIX.length());
        }

        // 2. Try to get token from cookie
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> JWT_COOKIE_NAME.equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        return null;
    }

    /**
     * Sets the JWT token in the response as both a cookie and a header.
     */
    private void setTokenInResponse(HttpServletResponse response, String token) {
        // Set token in Authorization header
        response.setHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token);
        
        // Set token in HTTP-only cookie
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, token);
        cookie.setHttpOnly(httpOnlyCookie);
        cookie.setSecure(secureCookie);
        cookie.setPath("/");
        
        if (StringUtils.hasText(cookieDomain)) {
            cookie.setDomain(cookieDomain);
        }
        
        // Set cookie to expire when the token expires
        int maxAge = (int) TimeUnit.MILLISECONDS.toSeconds(SecurityConstants.JWT_EXPIRATION_MS);
        cookie.setMaxAge(maxAge);
        
        // Add SameSite attribute via response header
        String sameSite = secureCookie ? 
            "; SameSite=None" : 
            "; SameSite=Lax";
            
        response.setHeader("Set-Cookie", 
            String.format("%s=%s; Path=/; Max-Age=%d; %s%s%s",
                JWT_COOKIE_NAME,
                token,
                maxAge,
                secureCookie ? "Secure; " : "",
                httpOnlyCookie ? "HttpOnly; " : "",
                sameSite
            )
        );
    }
}
