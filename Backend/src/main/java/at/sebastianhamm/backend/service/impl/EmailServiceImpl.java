package at.sebastianhamm.backend.service.impl;

import at.sebastianhamm.backend.model.User;
import at.sebastianhamm.backend.service.EmailService;
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

import java.util.Date;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.url}")
    private String appUrl;

    @Async
    @Override
    public void sendWelcomeEmail(User user) {
        if (user == null || user.getEmail() == null) {
            throw new IllegalArgumentException("User and user email must not be null");
        }

        try {
            final Context ctx = new Context(Locale.GERMAN);
            ctx.setVariable("name", user.getFirstName() + " " + user.getLastName());
            ctx.setVariable("appUrl", appUrl);

            final String htmlContent = templateEngine.process("emails/welcome", ctx);

            sendEmail(user.getEmail(), "Willkommen bei Blasmusik Eisenstadt", htmlContent, true);
            log.info("Welcome email sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}", user.getEmail(), e);
            throw new RuntimeException("Failed to send welcome email", e);
        }
    }

    @Async
    @Override
    public void sendOtpEmail(User user, String otpCode) {
        if (user == null || user.getEmail() == null || otpCode == null || otpCode.trim().isEmpty()) {
            throw new IllegalArgumentException("User, user email and OTP code must not be null or empty");
        }

        try {
            final Context ctx = new Context(Locale.GERMAN);
            ctx.setVariable("name", user.getFirstName() + " " + user.getLastName());
            ctx.setVariable("otpCode", otpCode);
            ctx.setVariable("appUrl", appUrl);

            final String htmlContent = templateEngine.process("emails/otp", ctx);

            sendEmail(user.getEmail(), "Ihr Einmalpasswort für Blasmusik Eisenstadt", htmlContent, true);
            log.info("OTP email sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}", user.getEmail(), e);
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    @Async
    @Override
    public void sendPasswordResetEmail(User user, String resetToken) {
        if (user == null || user.getEmail() == null || resetToken == null || resetToken.trim().isEmpty()) {
            throw new IllegalArgumentException("User, user email and reset token must not be null or empty");
        }

        try {
            final Context ctx = new Context(Locale.GERMAN);
            ctx.setVariable("name", user.getFirstName() + " " + user.getLastName());
            ctx.setVariable("resetUrl", appUrl + "/reset-password?token=" + resetToken);
            ctx.setVariable("appUrl", appUrl);

            final String htmlContent = templateEngine.process("emails/password-reset", ctx);

            sendEmail(user.getEmail(), "Passwort zurücksetzen - Blasmusik Eisenstadt", htmlContent, true);
            log.info("Password reset email sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}", user.getEmail(), e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    @Async
    @Override
    public void sendVerificationEmail(User user, String verificationToken) {
        if (user == null || user.getEmail() == null || verificationToken == null || verificationToken.trim().isEmpty()) {
            throw new IllegalArgumentException("User, user email and verification token must not be null or empty");
        }

        try {
            final Context ctx = new Context(Locale.GERMAN);
            ctx.setVariable("name", user.getFirstName() + " " + user.getLastName());
            ctx.setVariable("verificationUrl", appUrl + "/verify-email?token=" + verificationToken);
            ctx.setVariable("appUrl", appUrl);

            final String htmlContent = templateEngine.process("emails/verification", ctx);

            sendEmail(user.getEmail(), "Bitte bestätigen Sie Ihre E-Mail-Adresse - Blasmusik Eisenstadt", htmlContent, true);
            log.info("Verification email sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send verification email to {}", user.getEmail(), e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    @Async
    @Override
    public void sendAccountLockedEmail(User user) {
        if (user == null || user.getEmail() == null) {
            throw new IllegalArgumentException("User and user email must not be null");
        }

        try {
            final Context ctx = new Context(Locale.GERMAN);
            ctx.setVariable("name", user.getFirstName() + " " + user.getLastName());
            ctx.setVariable("appUrl", appUrl);

            final String htmlContent = templateEngine.process("emails/account-locked", ctx);

            sendEmail(user.getEmail(), "Ihr Konto wurde gesperrt - Blasmusik Eisenstadt", htmlContent, true);
            log.info("Account locked email sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send account locked email to {}", user.getEmail(), e);
            throw new RuntimeException("Failed to send account locked email", e);
        }
    }

    @Async
    @Override
    public void sendAccountUnlockedEmail(User user) {
        if (user == null || user.getEmail() == null) {
            throw new IllegalArgumentException("User and user email must not be null");
        }

        try {
            final Context ctx = new Context(Locale.GERMAN);
            ctx.setVariable("name", user.getFirstName() + " " + user.getLastName());
            ctx.setVariable("appUrl", appUrl);

            final String htmlContent = templateEngine.process("emails/account-unlocked", ctx);

            sendEmail(user.getEmail(), "Ihr Konto wurde entsperrt - Blasmusik Eisenstadt", htmlContent, true);
            log.info("Account unlocked email sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send account unlocked email to {}", user.getEmail(), e);
            throw new RuntimeException("Failed to send account unlocked email", e);
        }
    }

    @Async
    @Override
    public void sendNewLoginAlert(User user, String deviceInfo, String locationInfo) {
        if (user == null || user.getEmail() == null) {
            throw new IllegalArgumentException("User and user email must not be null");
        }

        try {
            final Context ctx = new Context(Locale.GERMAN);
            ctx.setVariable("name", user.getFirstName() + " " + user.getLastName());
            ctx.setVariable("deviceInfo", deviceInfo != null ? deviceInfo : "Unbekanntes Gerät");
            ctx.setVariable("locationInfo", locationInfo != null ? locationInfo : "Unbekannter Standort");
            ctx.setVariable("timestamp", new Date());
            ctx.setVariable("appUrl", appUrl);

            final String htmlContent = templateEngine.process("emails/new-login-alert", ctx);

            sendEmail(user.getEmail(), "Neue Anmeldung erkannt - Blasmusik Eisenstadt", htmlContent, true);
            log.info("New login alert sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send new login alert to {}", user.getEmail(), e);
            throw new RuntimeException("Failed to send new login alert", e);
        }
    }

    @Async
    @Override
    public void sendEventNotification(User user, String eventTitle, String eventDate,
                                     String eventLocation, String additionalInfo) {
        if (user == null || user.getEmail() == null || eventTitle == null || eventTitle.trim().isEmpty() ||
            eventDate == null || eventDate.trim().isEmpty()) {
            throw new IllegalArgumentException("User, user email, event title and event date must not be null or empty");
        }

        try {
            final Context ctx = new Context(Locale.GERMAN);
            ctx.setVariable("name", user.getFirstName() + " " + user.getLastName());
            ctx.setVariable("eventTitle", eventTitle);
            ctx.setVariable("eventDate", eventDate);
            ctx.setVariable("eventLocation", eventLocation != null ? eventLocation : "Nicht angegeben");
            ctx.setVariable("additionalInfo", additionalInfo != null ? additionalInfo : "");
            ctx.setVariable("appUrl", appUrl);

            final String htmlContent = templateEngine.process("emails/event-notification", ctx);

            sendEmail(user.getEmail(), "Anstehende Veranstaltung: " + eventTitle + " - Blasmusik Eisenstadt", htmlContent, true);
            log.info("Event notification sent to {} for event: {}", user.getEmail(), eventTitle);
        } catch (Exception e) {
            log.error("Failed to send event notification to {} for event: {}", user.getEmail(), eventTitle, e);
            throw new RuntimeException("Failed to send event notification", e);
        }
    }

    @Async
    @Override
    public void sendSystemAlert(String subject, String message, String errorDetails) {
        try {
            final Context ctx = new Context(Locale.GERMAN);
            ctx.setVariable("message", message);
            ctx.setVariable("errorDetails", errorDetails != null ? errorDetails : "Keine weiteren Details verfügbar");
            ctx.setVariable("timestamp", new Date());

            final String htmlContent = templateEngine.process("emails/system-alert", ctx);

            sendEmail(fromEmail, "[SYSTEM ALERT] " + subject, htmlContent, true);
            log.info("System alert email sent to admin");
        } catch (Exception e) {
            log.error("Failed to send system alert email", e);
            throw new RuntimeException("Failed to send system alert email", e);
        }
    }

    @Override
    public void sendEmail(String to, String subject, String content, boolean isHtml) {
        if (to == null || to.trim().isEmpty() || subject == null || subject.trim().isEmpty() || content == null) {
            throw new IllegalArgumentException("To, subject and content must not be null or empty");
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            
            // Add inline images if needed
            // FileSystemResource res = new FileSystemResource(new File("path/to/logo.png"));
            // message.addInline("logo", res);
            
            mailSender.send(mimeMessage);
            log.debug("Email sent to {} with subject: {}", to, subject);
        } catch (MessagingException e) {
            log.error("Failed to send email to {} with subject: {}", to, subject, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
