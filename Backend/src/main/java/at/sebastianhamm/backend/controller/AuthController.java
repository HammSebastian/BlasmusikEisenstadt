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
import jakarta.servlet.http.Cookie;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            User user = userDetailsService.getUserByEmail(userDetails.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (user.isOtpEnabled()) {
                if (request.getOtp() == null) {
                    return ResponseEntity.ok(AuthenticationResponse.builder().requiresOtp(true).build());
                }
                AuthenticationResponse otpResponse = authService.verifyOtp(new AuthenticationRequest(user.getEmail(), request.getOtp()));
                if (!otpResponse.isSuccess()) {
                    return ResponseEntity.badRequest().body("Invalid OTP");
                }
            }

            String jwtToken = jwtService.generateToken(authentication);
            String refreshToken = jwtService.generateRefreshToken(authentication);

            ResponseCookie jwtCookie = createCookie("jwtToken", jwtToken, "/", 24 * 60 * 60);
            ResponseCookie refreshCookie = createCookie("refreshToken", refreshToken, "/api/auth/refresh-token", 7 * 24 * 60 * 60);

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
            if (refreshToken == null || !jwtService.validateToken(refreshToken)) {
                throw new BadRequestException("Invalid refresh token");
            }

            String username = jwtService.getUsernameFromToken(refreshToken);
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

            String newJwtToken = jwtService.generateToken(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
            String newRefreshToken = jwtService.generateRefreshToken(
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

            ResponseCookie jwtCookie = createCookie("jwtToken", newJwtToken, "/", 24 * 60 * 60);
            ResponseCookie refreshCookie = createCookie("refreshToken", newRefreshToken, "/api/auth/refresh-token", 7 * 24 * 60 * 60);

            User user = userDetailsService.getUserByEmail(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(createAuthResponse(user, newJwtToken, newRefreshToken));

        } catch (Exception e) {
            throw new BadRequestException("Failed to refresh token: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie jwtCookie = createCookie("jwtToken", "", "/", 0);
        ResponseCookie refreshCookie = createCookie("refreshToken", "", "/api/auth/refresh-token", 0);

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
    public ResponseEntity<Void> requestPasswordReset(@RequestParam String email) {
        authService.requestPasswordReset(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthenticationResponse> verifyOtp(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/request-otp")
    public ResponseEntity<Void> requestNewOtp(@RequestParam String email) {
        authService.requestNewOtp(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();

        UserDto userDto = authService.getCurrentUser(email);
        return ResponseEntity.ok(userDto);
    }

    private ResponseCookie createCookie(String name, String token, String path, int maxAgeSeconds) {
        return ResponseCookie.from(name, token)
                .httpOnly(true)
                .secure(false) // Set to true in production with HTTPS
                .path(path)
                .maxAge(maxAgeSeconds)
                .sameSite("Strict")
                .build();
    }

    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        String refreshToken = null;

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            refreshToken = authHeader.substring(7);
        } else if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
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
                .success(true)
                .build();
    }
}
