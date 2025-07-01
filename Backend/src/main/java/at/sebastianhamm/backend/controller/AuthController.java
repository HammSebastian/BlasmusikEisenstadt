package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.dto.AuthenticationRequest;
import at.sebastianhamm.backend.dto.AuthenticationResponse;
import at.sebastianhamm.backend.dto.UserDto;
import at.sebastianhamm.backend.exception.BadRequestException;
import at.sebastianhamm.backend.exception.ResourceNotFoundException;
import at.sebastianhamm.backend.model.User;
import at.sebastianhamm.backend.security.UserDetailsImpl;
import at.sebastianhamm.backend.security.jwt.JwtService;
import at.sebastianhamm.backend.service.AuthenticationService;
import at.sebastianhamm.backend.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthenticationRequest request) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Check if OTP is required
            User user = userDetailsService.getUserByEmail(userDetails.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (user.isOtpEnabled() && request.getOtp() == null) {
                // OTP is required but not provided
                return ResponseEntity.ok(AuthenticationResponse.builder()
                    .requiresOtp(true)
                    .build());
            }

            // If OTP is enabled, verify it
            if (user.isOtpEnabled()) {
                AuthenticationRequest authRequest = new AuthenticationRequest(user.getEmail(), request.getOtp());
                AuthenticationResponse response = authService.verifyOtp(authRequest);

            }

            // Generate tokens
            String jwtToken = jwtService.generateJwtToken(authentication);
            String refreshToken = jwtService.generateRefreshToken(authentication);
            
            // Create HTTP-only cookie for refresh token
            ResponseCookie jwtCookie = createJwtCookie(jwtToken);
            ResponseCookie refreshCookie = createRefreshCookie(refreshToken);

            return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(createAuthResponse(user, jwtToken, refreshToken));

        } catch (Exception e) {
            throw new BadRequestException("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        try {
            String refreshToken = getRefreshTokenFromRequest(request);
            if (refreshToken == null || !jwtService.validateJwtToken(refreshToken)) {
                throw new BadRequestException("Invalid refresh token");
            }

            String username = jwtService.getUserNameFromJwtToken(refreshToken);
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
            
            // Generate new tokens
            String newJwtToken = jwtService.generateTokenFromUsername(username);
            String newRefreshToken = jwtService.generateRefreshToken(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

            // Create HTTP-only cookies
            ResponseCookie jwtCookie = createJwtCookie(newJwtToken);
            ResponseCookie refreshCookie = createRefreshCookie(newRefreshToken);

            return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(createAuthResponse(
                    userDetailsService.getUserByEmail(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")),
                    newJwtToken,
                    newRefreshToken
                ));

        } catch (Exception e) {
            throw new BadRequestException("Failed to refresh token: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // Create empty cookies to clear the tokens
        ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", "")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(0)
            .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(0)
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .body("You've been signed out!");
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody UserDto userDto,
            @RequestHeader("X-Admin-Email") String adminEmail) {
        
        UserDto createdUser = authService.register(userDto, adminEmail);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/request-password-reset")
    public void requestPasswordReset(@RequestParam String email) {
        authService.requestPasswordReset(email);
    }

    @PostMapping("/reset-password")
    public void resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        
        authService.resetPassword(token, newPassword);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthenticationResponse> verifyOtp(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/request-otp")
    public void requestNewOtp(@RequestParam String email) {
        authService.requestNewOtp(email);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        UserDto userDto = authService.getCurrentUser(email);
        return ResponseEntity.ok(userDto);
    }

    private ResponseCookie createJwtCookie(String token) {
        return ResponseCookie.from("jwtToken", token)
            .httpOnly(true)
            .secure(false) // Set to true in production with HTTPS
            .path("/")
            .maxAge(24 * 60 * 60) // 1 day
            .sameSite("Strict")
            .build();
    }

    private ResponseCookie createRefreshCookie(String token) {
        return ResponseCookie.from("refreshToken", token)
            .httpOnly(true)
            .secure(false) // Set to true in production with HTTPS
            .path("/api/auth/refresh-token")
            .maxAge(7 * 24 * 60 * 60) // 7 days
            .sameSite("Strict")
            .build();
    }

    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        String refreshToken = null;
        
        // Try to get from Authorization header first
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            refreshToken = authHeader.substring(7);
        } 
        // Then try to get from cookie
        else if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        
        return refreshToken;
    }

    private AuthenticationResponse createAuthResponse(User user, String jwtToken, String refreshToken) {
        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .user(UserDto.fromUser(user))
            .requiresOtp(false)
            .build();
    }
}
