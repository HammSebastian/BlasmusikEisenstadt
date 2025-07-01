package at.sebastianhamm.backend.service.impl;

import at.sebastianhamm.backend.dto.AuthenticationRequest;
import at.sebastianhamm.backend.dto.AuthenticationResponse;
import at.sebastianhamm.backend.dto.UserDto;
import at.sebastianhamm.backend.exception.ResourceNotFoundException;
import at.sebastianhamm.backend.model.User;
import at.sebastianhamm.backend.repository.UserRepository;
import at.sebastianhamm.backend.security.JwtService;
import at.sebastianhamm.backend.service.AuthenticationService;
import at.sebastianhamm.backend.service.EmailService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final GoogleAuthenticator googleAuthenticator;

    @Override
    @Transactional
    public UserDto register(UserDto userDto, String adminEmail) {
        // Only admins can register new users
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        
        if (!User.Role.ROLE_ADMIN.equals(admin.getRole())) {
            throw new SecurityException("Only admins can register new users");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalStateException("Email already in use");
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode("temporaryPassword")) // Will be changed on first login
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .role(userDto.getRole() != null ? userDto.getRole() : User.Role.ROLE_USER)
                .enabled(true)
                .otpEnabled(true)
                .emailNotifications(true)
                .build();

        // Generate OTP secret for the user
        final GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        user.setOtpSecret(key.getKey());
        
        User savedUser = userRepository.save(user);
        
        // Send welcome email with setup instructions
        emailService.sendWelcomeEmail(user);
        
        return UserDto.fromUser(savedUser);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // If OTP is required but not provided
        if (user.isOtpEnabled() && request.getOtp() == null) {
            return AuthenticationResponse.builder()
                    .requiresOtp(true)
                    .build();
        }

        // Verify OTP if enabled
        if (user.isOtpEnabled()) {
            if (!googleAuthenticator.authorize(user.getOtpSecret(), Integer.parseInt(request.getOtp()))) {
                throw new SecurityException("Invalid OTP");
            }
        }

        // Generate JWT token with proper authentication object
        String jwtToken = jwtService.generateToken(authentication);
        
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .user(UserDto.fromUser(user))
                .requiresOtp(false)
                .build();
    }

    @Override
    public AuthenticationResponse verifyOtp(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isOtpEnabled()) {
            throw new IllegalStateException("OTP is not enabled for this user");
        }

        if (!googleAuthenticator.authorize(user.getOtpSecret(), Integer.parseInt(request.getOtp()))) {
            throw new SecurityException("Invalid OTP");
        }

        // Generate JWT token with proper authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user.getUsername(),
            user.getPassword(),
            user.getAuthorities()
        );
        String jwtToken = jwtService.generateToken(authentication);
        
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .user(UserDto.fromUser(user))
                .requiresOtp(false)
                .build();
    }

    @Override
    public void requestNewOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isOtpEnabled()) {
            throw new IllegalStateException("OTP is not enabled for this user");
        }

        // In a real app, you might want to implement rate limiting here
        String otp = String.valueOf(generateOtp(user.getOtpSecret()));
        emailService.sendOtpEmail(user, otp);
    }

    @Override
    @Transactional
    public UserDto toggleOtp(String email, boolean enabled) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (enabled && user.getOtpSecret() == null) {
            // Generate new OTP secret if enabling OTP for the first time
            final GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
            user.setOtpSecret(key.getKey());
        }
        
        user.setOtpEnabled(enabled);
        User updatedUser = userRepository.save(user);
        
        return UserDto.fromUser(updatedUser);
    }

    @Override
    @Transactional
    public UserDto toggleEmailNotifications(String email, boolean enabled) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setEmailNotifications(enabled);
        User updatedUser = userRepository.save(user);
        
        return UserDto.fromUser(updatedUser);
    }

    @Override
    public UserDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserDto.fromUser(user);
    }

    @Override
    @Transactional
    public UserDto updateProfile(String email, UserDto userDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        
        User updatedUser = userRepository.save(user);
        return UserDto.fromUser(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new SecurityException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Create authentication for reset token
        Authentication auth = new UsernamePasswordAuthenticationToken(
            user.getEmail(),
            "",
            Collections.singletonList(() -> "RESET_PASSWORD")
        );
        
        // Generate reset token
        String resetToken = jwtService.generateToken(auth);
        
        // Send reset email
        emailService.sendPasswordResetEmail(user, resetToken);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        try {
            String email = jwtService.getUsernameFromToken(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
            // In a real app, you'd also verify the token hasn't been used before
            // and is not expired
            
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            
            // Invalidate the token after use
            // (in a real app, you'd store used tokens in a blacklist)
            
        } catch (Exception e) {
            throw new SecurityException("Invalid or expired reset token");
        }
    }
    
    private String generateOtp(String secret) {
        return String.format("%06d", googleAuthenticator.getTotpPassword(secret));
    }
}
