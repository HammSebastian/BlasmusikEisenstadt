package at.sebastianhamm.backend.service;

import at.sebastianhamm.backend.io.ProfileRequest;
import at.sebastianhamm.backend.io.ProfileResponse;

public interface ProfileService {

    /**
     * Creates a new user profile
     * @param profileRequest The profile information
     * @return The created profile
     * @throws org.springframework.web.server.ResponseStatusException with CONFLICT status if email already exists
     */
    ProfileResponse createProfile(ProfileRequest profileRequest);

    /**
     * Retrieves a user profile by email
     * @param email The email of the user
     * @return The user profile
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException if user not found
     */
    ProfileResponse getProfile(String email);

    /**
     * Sends a password reset OTP to the user's email
     * @param email The email of the user
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException if user not found
     */
    void sendResetOtp(String email);

    /**
     * Resets the user's password using a valid OTP
     * @param email The email of the user
     * @param otp The one-time password
     * @param newPassword The new password
     * @throws org.springframework.web.server.ResponseStatusException with BAD_REQUEST if OTP is invalid or expired
     */
    void resetPassword(String email, String otp, String newPassword);

    /**
     * Verifies a user's account using an OTP
     * @param email The email of the user
     * @param otp The one-time password
     * @throws org.springframework.web.server.ResponseStatusException with BAD_REQUEST if OTP is invalid or expired
     */
    void verifyAccount(String email, String otp);

    /**
     * Resends the verification OTP to the user's email
     * @param email The email of the user
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException if user not found
     */
    void resendVerificationOtp(String email);

    /**
     * Updates a user's profile information
     * @param email The email of the user to update
     * @param profileRequest The updated profile information
     * @throws org.springframework.web.server.ResponseStatusException with CONFLICT status if new email is already in use
     */
    void updateProfile(String email, ProfileRequest profileRequest);
}
