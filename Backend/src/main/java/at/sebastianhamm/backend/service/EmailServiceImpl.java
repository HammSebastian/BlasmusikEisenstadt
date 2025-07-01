package at.sebastianhamm.backend.service;

import at.sebastianhamm.backend.config.EmailConfig;
import at.sebastianhamm.backend.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final EmailConfig emailConfig;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.admin-email}")
    private String adminEmail;

    @Async
    @Override
    public void sendWelcomeEmail(User user) {
        try {
            String subject = "Welcome to Blasmusik Eisenstadt";
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", user.getFullName());
            variables.put("email", user.getEmail());
            variables.put("supportEmail", emailConfig.getSupportEmail());
            variables.put("frontendUrl", frontendUrl);
            
            String content = buildEmailContent("emails/welcome-email", variables);
            sendEmail(user.getEmail(), subject, content, true);
            log.info("Welcome email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send welcome email to: " + user.getEmail(), e);
            sendSystemAlert("Failed to Send Welcome Email", 
                          "Failed to send welcome email to: " + user.getEmail(), 
                          e.getMessage());
        }
    }

    @Async
    @Override
    public void sendOtpEmail(User user, String otpCode) {
        try {
            String subject = "Your One-Time Password (OTP) for Blasmusik Eisenstadt";
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", user.getFullName());
            variables.put("otpCode", otpCode);
            variables.put("expiryMinutes", 10); // Should come from config
            
            String content = buildEmailContent("emails/otp-email", variables);
            sendEmail(user.getEmail(), subject, content, true);
            log.info("OTP email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send OTP email to: " + user.getEmail(), e);
            // Don't send system alert for OTP failures to avoid alert fatigue
        }
    }

    @Async
    @Override
    public void sendPasswordResetEmail(User user, String resetToken) {
        try {
            String subject = "Password Reset Request - Blasmusik Eisenstadt";
            String resetUrl = String.format("%s/reset-password?token=%s", frontendUrl, resetToken);
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", user.getFullName());
            variables.put("resetUrl", resetUrl);
            variables.put("expiryHours", 24); // Should come from config
            
            String content = buildEmailContent("emails/password-reset-email", variables);
            sendEmail(user.getEmail(), subject, content, true);
            log.info("Password reset email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send password reset email to: " + user.getEmail(), e);
            sendSystemAlert("Failed to Send Password Reset Email", 
                          "Failed to send password reset email to: " + user.getEmail(), 
                          e.getMessage());
        }
    }

    @Override
    public void sendVerificationEmail(User user, String verificationToken) {
        try {
            String subject = "Email Verification - Blasmusik Eisenstadt";
            String verificationUrl = String.format("%s/verify-email?token=%s", frontendUrl, verificationToken);

            Map<String, Object> variables = new HashMap<>();
            variables.put("name", user.getFullName());
            variables.put("verificationUrl", verificationUrl);
            variables.put("expiryHours", 24); // Should come from config

            String content = buildEmailContent("emails/verification-email", variables);
            sendEmail(user.getEmail(), subject, content, true);
            log.info("Verification email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send verification email to: " + user.getEmail(), e);
            sendSystemAlert("Failed to Send Verification Email",
                          "Failed to send verification email to: " + user.getEmail(),
                          e.getMessage());
        }
    }

    @Async
    @Override
    public void sendAccountLockedEmail(User user) {
        try {
            String subject = "Account Locked - Blasmusik Eisenstadt";
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", user.getFullName());
            variables.put("lockTime", user.getLockTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            variables.put("unlockTime", user.getLockTime().plusMinutes(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            variables.put("supportEmail", emailConfig.getSupportEmail());
            
            String content = buildEmailContent("emails/account-locked-email", variables);
            sendEmail(user.getEmail(), subject, content, true);
            log.info("Account locked email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send account locked email to: " + user.getEmail(), e);
            sendSystemAlert("Failed to Send Account Locked Email", 
                          "Failed to send account locked email to: " + user.getEmail(), 
                          e.getMessage());
        }
    }

    @Async
    @Override
    public void sendAccountUnlockedEmail(User user) {
        try {
            String subject = "Account Unlocked - Blasmusik Eisenstadt";
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", user.getFullName());
            variables.put("unlockTime", user.getUpdatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            variables.put("supportEmail", emailConfig.getSupportEmail());
            
            String content = buildEmailContent("emails/account-unlocked-email", variables);
            sendEmail(user.getEmail(), subject, content, true);
            log.info("Account unlocked email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send account unlocked email to: " + user.getEmail(), e);
            // Non-critical, no need for system alert
        }
    }

    @Async
    @Override
    public void sendNewLoginAlert(User user, String deviceInfo, String locationInfo) {
        try {
            String subject = "New Login Detected - Blasmusik Eisenstadt";
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", user.getFullName());
            variables.put("loginTime", user.getLastLogin().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            variables.put("deviceInfo", deviceInfo);
            variables.put("locationInfo", locationInfo);
            variables.put("supportEmail", emailConfig.getSupportEmail());
            
            String content = buildEmailContent("emails/new-login-alert-email", variables);
            sendEmail(user.getEmail(), subject, content, true);
            log.info("New login alert sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send new login alert to: " + user.getEmail(), e);
            // Non-critical, no need for system alert
        }
    }

    @Async
    @Override
    public void sendEventNotification(User user, String eventTitle, String eventDate, 
                                     String eventLocation, String additionalInfo) {
        try {
            String subject = String.format("Upcoming Event: %s - Blasmusik Eisenstadt", eventTitle);
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", user.getFullName());
            variables.put("eventTitle", eventTitle);
            variables.put("eventDate", eventDate);
            variables.put("eventLocation", eventLocation);
            variables.put("additionalInfo", additionalInfo);
            variables.put("frontendUrl", frontendUrl);
            
            String content = buildEmailContent("emails/event-notification-email", variables);
            sendEmail(user.getEmail(), subject, content, true);
            log.info("Event notification sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send event notification to: " + user.getEmail(), e);
            // Non-critical, no need for system alert
        }
    }

    @Override
    public void sendEmail(String to, String subject, String content, boolean isHtml) {
        if (to == null || to.isBlank() || subject == null || subject.isBlank() || content == null || content.isBlank()) {
            throw new IllegalArgumentException("Email parameters cannot be null or empty");
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                message, 
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
            );

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, isHtml);

            mailSender.send(message);
            log.debug("Email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to: " + to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Async
    @Override
    public void sendSystemAlert(String subject, String message, String errorDetails) {
        if (adminEmail == null || adminEmail.isBlank()) {
            log.warn("No admin email configured for system alerts");
            return;
        }

        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("subject", subject);
            variables.put("message", message);
            variables.put("errorDetails", errorDetails);
            variables.put("timestamp", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(java.time.LocalDateTime.now()));
            
            String content = buildEmailContent("emails/system-alert-email", variables);
            sendEmail(adminEmail, "[System Alert] " + subject, content, true);
            log.info("System alert sent to admin: {}", adminEmail);
        } catch (Exception e) {
            log.error("Failed to send system alert to admin: " + adminEmail, e);
            // Last resort - log to error stream
            System.err.println("CRITICAL: Failed to send system alert. Reason: " + e.getMessage());
        }
    }

    private String buildEmailContent(String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            if (variables != null) {
                variables.forEach(context::setVariable);
            }
            return templateEngine.process(templateName, context);
        } catch (Exception e) {
            log.error("Failed to process email template: " + templateName, e);
            throw new RuntimeException("Failed to generate email content", e);
        }
    }
}
