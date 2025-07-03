package at.sebastianhamm.backend.service;

import at.sebastianhamm.backend.dto.*;

public interface AuthenticationService {

    /**
     * Register a new user (admin only).
     */
    UserDto register(UserDto userDto, String adminEmail);

    /**
     * Authenticate user with email and password.
     * If OTP is enabled, response contains requiresOtp=true, token=null.
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);

    /**
     * Verify OTP code and complete authentication.
     * Returns JWT token upon successful verification.
     */
    AuthenticationResponse verifyOtp(AuthenticationRequest request);

    /**
     * Generate and send a new OTP code via email.
     */
    void requestNewOtp(String email);

    /**
     * Enable or disable OTP for the user.
     */
    UserDto toggleOtp(String email, boolean enabled);

    /**
     * Enable or disable email notifications for the user.
     */
    UserDto toggleEmailNotifications(String email, boolean enabled);

    /**
     * Retrieve current user's profile by email.
     */
    UserDto getCurrentUser(String email);

    /**
     * Update user's profile data.
     */
    UserDto updateProfile(String email, UserDto userDto);

    /**
     * Change the user's password.
     */
    void changePassword(String email, String currentPassword, String newPassword);

    /**
     * Request password reset by sending a reset token to the user's email.
     */
    void requestPasswordReset(String email);

    /**
     * Reset password using provided token.
     */
    void resetPassword(String token, String newPassword);
}
