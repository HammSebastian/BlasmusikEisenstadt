package at.sebastianhamm.backend.security;

import at.sebastianhamm.backend.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtils {
    private final JwtService jwtService;

    public JwtUtils(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (StringUtils.isNotEmpty(header) && header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return header.substring(SecurityConstants.TOKEN_PREFIX.length());
        }
        return null;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new JwtAuthenticationException("Invalid JWT token", e);
        }
    }

    public String generateToken(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }

    public String generateRefreshToken(Authentication authentication) {
        return jwtService.generateRefreshToken(authentication);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtService.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new JwtAuthenticationException("JWT token is expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new JwtAuthenticationException("JWT token is unsupported", e);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new JwtAuthenticationException("Invalid JWT token", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new JwtAuthenticationException("JWT claims string is empty", e);
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateTokenStructure(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtService.getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token structure: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenBlacklisted(String token) {
        // Implement token blacklist check if needed
        return false;
    }

    public String getUsernameFromToken(String token) {
        return extractUsername(token);
    }

    public boolean isTokenExpiringSoon(String token) {
        try {
            Date expiration = extractExpiration(token);
            Date now = new Date();
            long timeUntilExpiry = expiration.getTime() - now.getTime();
            // Consider token expiring soon if it expires in less than 5 minutes
            return timeUntilExpiry < (5 * 60 * 1000);
        } catch (Exception e) {
            log.error("Error checking if token is expiring soon: {}", e.getMessage());
            return true; // If we can't check, assume it's expiring soon
        }
    }

    public String refreshToken(String token) {
        try {
            String username = getUsernameFromToken(token);
            // Create a simple authentication object with just the username
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                username, 
                null, 
                Collections.emptyList()
            );
            return jwtService.generateToken(authentication);
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage());
            throw new JwtAuthenticationException("Failed to refresh token", e);
        }
    }
}
