package at.sebastianhamm.backend.security.jwt;

import at.sebastianhamm.backend.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Component
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKeyBase64;

    @Value("${jwt.token.expiration}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshExpirationMs;

    private Key signingKey;

    @PostConstruct
    protected void init() {
        log.info("JWT Secret (Base64): {}", secretKeyBase64);

        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalStateException("JWT secret key must be configured");
        }
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64);
            log.info("Decoded key length: {}", keyBytes.length);

            if (keyBytes.length < 32) {
                throw new IllegalStateException("JWT secret key must be at least 256 bits (32 bytes)");
            }
            signingKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("JWT secret key is not valid Base64", ex);
        }
    }


    // Token mit Rollenclaims erzeugen
    public String generateToken(Authentication authentication) {
        return generateToken(authentication.getName(), authentication.getAuthorities(), jwtExpirationMs);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication.getName(), Collections.emptyList(), refreshExpirationMs);
    }

    private String generateToken(String username, Collection<? extends GrantedAuthority> authorities, long expirationMs) {
        Map<String, Object> claims = new HashMap<>();
        if (authorities != null && !authorities.isEmpty()) {
            claims.put("roles", authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
        }
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // Token validieren gegen UserDetails
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()));
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            log.warn("Expired JWT token: {}", ex.getMessage());
            throw new JwtAuthenticationException("JWT token expired", ex);
        } catch (UnsupportedJwtException ex) {
            log.warn("Unsupported JWT token: {}", ex.getMessage());
            throw new JwtAuthenticationException("Unsupported JWT token", ex);
        } catch (MalformedJwtException ex) {
            log.warn("Invalid JWT token: {}", ex.getMessage());
            throw new JwtAuthenticationException("Invalid JWT token", ex);
        } catch (SignatureException ex) {
            log.warn("Invalid JWT signature: {}", ex.getMessage());
            throw new JwtAuthenticationException("Invalid JWT signature", ex);
        } catch (IllegalArgumentException ex) {
            log.warn("JWT claims string is empty: {}", ex.getMessage());
            throw new JwtAuthenticationException("JWT claims string empty", ex);
        }
    }

    public Key getSigningKey() {
        return signingKey;
    }
}
