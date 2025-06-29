package at.sebastianhamm.backend.service.impl;

import at.sebastianhamm.backend.entitiy.UserEntity;
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
        UserEntity newProfile = convertToUserEntity(profileRequest);

        if (!userRepository.existsByEmail(profileRequest.getEmail())) {
            newProfile = userRepository.save(newProfile);
            return convertToProfileResponse(newProfile);
        }

        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
    }

    private ProfileResponse convertToProfileResponse(UserEntity newProfile) {
        return ProfileResponse.builder().
                userId(newProfile.getUserId()).
                name(newProfile.getName()).
                email(newProfile.getEmail()).
                isAccountVerified(newProfile.isAccountVerified()).
                build();
    }

    private UserEntity convertToUserEntity(ProfileRequest profileRequest) {
        return UserEntity.builder().email(
                        profileRequest.getEmail()).
                userId(UUID.randomUUID().toString()).
                name(profileRequest.getName()).
                password(passwordEncoder.encode(profileRequest.getPassword())).
                isAccountVerified(false).
                resetOtpExpiresAt(0L).
                resetOtp(null).
                verifyOtpExpiresAt(0L).
                verifyOtp(null).
                build();
    }

    @Override
    public ProfileResponse getProfile(String email) {
        UserEntity user = userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return convertToProfileResponse(user);
    }

    @Override
    public void sendResetOtp(String email) {
        UserEntity existingEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        String otp =String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        long expirationTime = System.currentTimeMillis() + (15*60*1000);
        existingEntity.setResetOtp(otp);
        existingEntity.setResetOtpExpiresAt(expirationTime);
        userRepository.save(existingEntity);

        try {
            emailService.sendResetOtpEmail(existingEntity.getEmail(), otp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        UserEntity existingEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (existingEntity.getResetOtp().equals(otp) && existingEntity.getResetOtpExpiresAt() > System.currentTimeMillis()) {
            existingEntity.setPassword(passwordEncoder.encode(newPassword));
            existingEntity.setResetOtp(null);
            existingEntity.setResetOtpExpiresAt(0L);
            userRepository.save(existingEntity);
        } else if (existingEntity.getVerifyOtp().equals(otp) && existingEntity.getVerifyOtpExpiresAt() > System.currentTimeMillis()) {
            existingEntity.setPassword(passwordEncoder.encode(newPassword));
            existingEntity.setVerifyOtp(null);
            existingEntity.setVerifyOtpExpiresAt(0L);
            userRepository.save(existingEntity);
        } else if (existingEntity.getResetOtpExpiresAt() < System.currentTimeMillis() && existingEntity.getVerifyOtpExpiresAt() < System.currentTimeMillis()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP has expired");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }
    }

    @Override
    public void sendOtp(String email) {
        UserEntity existingEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (existingEntity.isAccountVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account already verified");
        }

        String otp =String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        long expirationTime = System.currentTimeMillis() + (24*60*60*1000);
        existingEntity.setVerifyOtp(otp);
        existingEntity.setVerifyOtpExpiresAt(expirationTime);
        userRepository.save(existingEntity);

        try {
            emailService.sendOtpEmail(existingEntity.getEmail(), otp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {
        UserEntity existingEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (existingEntity.getVerifyOtp().equals(otp) && existingEntity.getVerifyOtpExpiresAt() > System.currentTimeMillis()) {
            existingEntity.setAccountVerified(true);
            existingEntity.setVerifyOtp(null);
            existingEntity.setVerifyOtpExpiresAt(0L);
            userRepository.save(existingEntity);
        } else if (existingEntity.getVerifyOtpExpiresAt() < System.currentTimeMillis()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP has expired");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }
    }
}
