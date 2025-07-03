package at.sebastianhamm.backend.service;

import at.sebastianhamm.backend.model.User;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {

    @Async
    void sendWelcomeEmail(User user);

    @Async
    void sendVerificationEmail(User user, String verificationToken);

    @Async
    void sendOtpEmail(User user, String otpCode);

    @Async
    void sendPasswordResetEmail(User user, String resetToken);

    @Async
    void sendAccountLockedEmail(User user);

    @Async
    void sendAccountUnlockedEmail(User user);

    @Async
    void sendNewLoginAlert(User user, String deviceInfo, String locationInfo);

    @Async
    void sendEventNotification(User user, String eventTitle, String eventDate, String eventLocation, String additionalInfo);

    @Async
    void sendSystemAlert(String subject, String message, String errorDetails);

    void sendEmail(String to, String subject, String content, boolean isHtml);
}
