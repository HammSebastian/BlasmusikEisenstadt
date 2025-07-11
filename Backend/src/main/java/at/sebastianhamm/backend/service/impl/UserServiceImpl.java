package at.sebastianhamm.backend.service.impl;

import at.sebastianhamm.backend.entity.User;
import at.sebastianhamm.backend.enums.Role;
import at.sebastianhamm.backend.exceptions.BadRequestException;
import at.sebastianhamm.backend.exceptions.NotFoundException;
import at.sebastianhamm.backend.payload.request.LoginRequest;
import at.sebastianhamm.backend.payload.request.RegisterRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.UserResponse;
import at.sebastianhamm.backend.repository.UserRepository;
import at.sebastianhamm.backend.security.CustomUserDetailsService;
import at.sebastianhamm.backend.security.JwtUtils;
import at.sebastianhamm.backend.security.RefreshTokenBlacklistService;
import at.sebastianhamm.backend.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenBlacklistService refreshTokenBlacklistService;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    public ApiResponse<?> signUp(RegisterRequest registerRequest) {
        log.info("Inside signUp()");
        Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());

        if (existingUser.isPresent()) {
            throw new BadRequestException("Username already taken");
        }

        User user = new User();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(Role.USER);
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        //save the user
        userRepository.save(user);

        return ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("user registered sucessfully")
                .build();

    }

    @Override
    public ApiResponse<?> login(LoginRequest loginRequest, HttpServletResponse response) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid Password");
        }

        String accessToken = jwtUtils.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());

        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(false)       // Later for production true
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("User logged in successfully")
                .build();
    }



    @Override
    public ApiResponse<User> getCurrentLoggedInUser() {
        String identifier = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Fetching user with identifier: {}", identifier);

        Optional<User> userOpt = userRepository.findByUsername(identifier);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(identifier);
        }

        User user = userOpt.orElseThrow(() ->
                new NotFoundException("User not found with identifier: " + identifier)
        );

        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "User retrieved successfully",
                user
        );
    }


    @Override
    public ApiResponse<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();
                    long expiry = jwtUtils.extractExpiration(refreshToken).getTime();
                    refreshTokenBlacklistService.blacklistToken(refreshToken, expiry);
                }
            }
        }

        ResponseCookie deleteAccessCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie deleteRefreshCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefreshCookie.toString());

        return ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Logout erfolgreich")
                .build();
    }

    @Override
    public ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .message("Missing refresh token")
                    .build();
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refresh_token".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .message("Missing refresh token")
                    .build();
        }

        if (refreshTokenBlacklistService.isBlacklisted(refreshToken)) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .message("Refresh token is blacklisted")
                    .build();
        }

        String username;
        try {
            username = jwtUtils.getUsernameFromToken(refreshToken);
        } catch (Exception e) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .message("Refresh token is invalid")
                    .build();
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        if (!jwtUtils.isTokenValid(refreshToken, userDetails)) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .message("Refresh token is invalid")
                    .build();
        }

        String newAccessToken = jwtUtils.generateAccessToken(username);

        ResponseCookie newAccessCookie = ResponseCookie.from("access_token", newAccessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, newAccessCookie.toString());

        return ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Refresh token is valid")
                .build();
    }

    @Override
    public ApiResponse<UserResponse> currentLoggedInUser() {
        User user = getCurrentLoggedInUser().getData();
        if (user == null) {
            return new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                "User not found",
                null
            );
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "User fetched successfully",
                userResponse
        );
    }
}
