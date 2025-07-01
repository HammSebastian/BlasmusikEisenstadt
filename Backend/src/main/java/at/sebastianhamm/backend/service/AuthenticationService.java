package at.sebastianhamm.backend.service;

import at.sebastianhamm.backend.dto.AuthenticationRequest;
import at.sebastianhamm.backend.dto.AuthenticationResponse;
import at.sebastianhamm.backend.dto.UserDto;
import at.sebastianhamm.backend.model.User;

public interface AuthenticationService {
    
    /**
     * Register a new user (admin only)
     */
    UserDto register(UserDto userDto, String adminEmail);
    
    /**
     * Authenticate a user with email and password
     * @return AuthenticationResponse with JWT token if OTP is not required, otherwise requiresOtp=true
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);
    
    /**
     * Verify OTP and complete authentication
     */
    AuthenticationResponse verifyOtp(AuthenticationRequest request);
    
    /**
     * Generate a new OTP and send it via email
     */
    void requestNewOtp(String email);
    
    /**
     * Enable/disable OTP for a user
     */
    UserDto toggleOtp(String email, boolean enabled);
    
    /**
     * Enable/disable email notifications for a user
     */
    UserDto toggleEmailNotifications(String email, boolean enabled);
    
    /**
     * Get current user profile
     */
    UserDto getCurrentUser(String email);
    
    /**
     * Update user profile
     */
    UserDto updateProfile(String email, UserDto userDto);
    
    /**
     * Change password
     */
    void changePassword(String email, String currentPassword, String newPassword);
    
    /**
     * Request password reset
     */
    void requestPasswordReset(String email);
    
    /**
     * Reset password with token
     */
    void resetPassword(String token, String newPassword);
}
