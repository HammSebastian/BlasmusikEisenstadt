package at.sebastianhamm.backend.service;

import at.sebastianhamm.backend.model.User;
import org.springframework.scheduling.annotation.Async;

/**
 * Service interface for sending various types of emails to users.
 * All email sending operations are asynchronous and should be fire-and-forget.
 */
public interface EmailService {
    
    /**
     * Sends a welcome email to a new user with account activation instructions.
     * 
     * @param user the user to send the welcome email to
     * @throws IllegalArgumentException if user is null or has no email
     */
    void sendWelcomeEmail(User user);
    
    /**
     * Sends an email with a one-time password (OTP) for two-factor authentication.
     * 
     * @param user the user to send the OTP to
     * @param otpCode the one-time password code
     * @throws IllegalArgumentException if user is null or has no email, or otpCode is null/empty
     */
    void sendOtpEmail(User user, String otpCode);
    
    /**
     * Sends a password reset email with a secure reset link.
     * 
     * @param user the user requesting password reset
     * @param resetToken the secure token for password reset
     * @throws IllegalArgumentException if user is null or has no email, or resetToken is null/empty
     */
    void sendPasswordResetEmail(User user, String resetToken);

    @Async
    void sendVerificationEmail(User user, String verificationToken);

    /**
     * Sends an email notification when a user's account is locked due to too many failed login attempts.
     * 
     * @param user the user whose account was locked
     * @throws IllegalArgumentException if user is null or has no email
     */
    void sendAccountLockedEmail(User user);
    
    /**
     * Sends an email notification when a user's account is unlocked.
     * 
     * @param user the user whose account was unlocked
     * @throws IllegalArgumentException if user is null or has no email
     */
    void sendAccountUnlockedEmail(User user);
    
    /**
     * Sends an email notification when a successful login is detected from a new device/location.
     * 
     * @param user the user who logged in
     * @param deviceInfo information about the device used for login
     * @param locationInfo information about the location of the login
     * @throws IllegalArgumentException if user is null or has no email
     */
    void sendNewLoginAlert(User user, String deviceInfo, String locationInfo);
    
    /**
     * Sends an email notification about an upcoming gig or event.
     * 
     * @param user the user to notify
     * @param eventTitle the title of the event
     * @param eventDate the date and time of the event
     * @param eventLocation the location of the event
     * @param additionalInfo any additional information about the event
     * @throws IllegalArgumentException if any required parameter is null or empty
     */
    void sendEventNotification(User user, String eventTitle, String eventDate, 
                             String eventLocation, String additionalInfo);
    
    /**
     * Sends a generic notification email with HTML content support.
     * 
     * @param to the recipient's email address
     * @param subject the email subject
     * @param content the HTML content of the email
     * @param isHtml whether the content is HTML (true) or plain text (false)
     * @throws IllegalArgumentException if to, subject, or content is null/empty
     */
    void sendEmail(String to, String subject, String content, boolean isHtml);
    
    /**
     * Sends an email to the system administrators with an error or system alert.
     * 
     * @param subject the subject of the alert
     * @param message the detailed message of the alert
     * @param errorDetails any error details or stack traces
     * @throws IllegalArgumentException if subject or message is null/empty
     */
    void sendSystemAlert(String subject, String message, String errorDetails);
}
