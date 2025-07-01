package at.sebastianhamm.backend.service.impl;

import at.sebastianhamm.backend.model.User;
import at.sebastianhamm.backend.io.ProfileRequest;
import at.sebastianhamm.backend.io.ProfileResponse;
import at.sebastianhamm.backend.repository.UserRepository;
import at.sebastianhamm.backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public ProfileResponse createProfile(ProfileRequest profileRequest) {
        if (userRepository.existsByEmail(profileRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        
        User newUser = convertToUser(profileRequest);
        newUser = userRepository.save(newUser);
        return convertToProfileResponse(newUser);
    }

    private ProfileResponse convertToProfileResponse(User user) {
        return ProfileResponse.builder()
                .userId(user.getUserId())
                .name(user.getFullName())
                .email(user.getEmail())
                .isAccountVerified(user.isAccountVerified())
                .build();
    }

    private User convertToUser(ProfileRequest profileRequest) {
        return User.builder()
                .email(profileRequest.getEmail())
                .firstName(extractFirstName(profileRequest.getName()))
                .lastName(extractLastName(profileRequest.getName()))
                .password(profileRequest.getPassword())
                .role(User.Role.ROLE_USER)
                .enabled(false)
                .build();
    }
    
    private String extractFirstName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] parts = fullName.trim().split("\\s+");
        return parts[0];
    }
    
    private String extractLastName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] parts = fullName.trim().split("\\s+", 2);
        return parts.length > 1 ? parts[1] : "";
    }

    @Override
    public ProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return convertToProfileResponse(user);
    }

    @Override
    public void sendResetOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        user.setPasswordResetToken(otp);
        user.setPasswordResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
        user = userRepository.save(user);

        try {
            emailService.sendOtpEmail(String.valueOf(user), otp);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (user.getPasswordResetToken() != null && 
            user.getPasswordResetToken().equals(otp) && 
            user.getPasswordResetTokenExpiry() != null && 
            user.getPasswordResetTokenExpiry().isAfter(LocalDateTime.now())) {
            
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setPasswordResetToken(null);
            user.setPasswordResetTokenExpiry(null);
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }
    }

    @Override
    public void verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (user.getOtpSecret() != null && 
            user.getOtpSecret().equals(otp) && 
            user.getOtpExpiry() != null && 
            user.getOtpExpiry().isAfter(LocalDateTime.now())) {
            
            user.setEnabled(true);
            user.setOtpSecret(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
        } else if (user.getOtpExpiry() != null && user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP has expired");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }
    }

    @Override
    public void resendVerificationOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        user.setOtpSecret(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(15));
        user = userRepository.save(user);

        try {
            emailService.sendVerificationEmail(user, otp);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    @Override
    public void updateProfile(String email, ProfileRequest profileRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (!user.getEmail().equals(profileRequest.getEmail()) && 
            userRepository.existsByEmail(profileRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        user.setFirstName(extractFirstName(profileRequest.getName()));
        user.setLastName(extractLastName(profileRequest.getName()));
        user.setEmail(profileRequest.getEmail());
        userRepository.save(user);
    }
}
