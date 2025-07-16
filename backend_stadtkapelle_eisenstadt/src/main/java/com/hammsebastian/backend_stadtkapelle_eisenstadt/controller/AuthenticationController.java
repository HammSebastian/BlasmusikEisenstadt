package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.LoginRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.RegisterRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.LoginResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.RegisterResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.AuthenticationService;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.EmailService;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl.AppUserDetailsService;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest requestRequest) {
        RegisterResponse response = authenticationService.register(requestRequest);

        emailService.sendWelcomeEmail(requestRequest.getEmail(), "welcome-email", requestRequest.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<RegisterResponse>builder()
                        .message("User registered successfully")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            final String jwtToken = jwtUtil.generateToken(userDetails);
            ResponseCookie cookie = ResponseCookie.from("jwtToken", jwtToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(
                    ApiResponse.<LoginResponse>builder()
                            .message("Login successful")
                            .statusCode(HttpStatus.OK.value())
                            .data(new LoginResponse(loginRequest.getEmail(), jwtToken))
                            .build()
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.<LoginResponse>builder()
                            .message("Invalid email or password")
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .errors("Invalid email or password")
                            .build());
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.<LoginResponse>builder()
                            .message("Account is disabled")
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .errors("Account is disabled")
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<LoginResponse>builder()
                            .message("Authentication failed")
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .errors(e.getMessage())
                            .build());
        }
    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
