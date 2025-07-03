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

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.email.admin-email}")
    private String adminEmail;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Async
    @Override
    public void sendWelcomeEmail(User user) {
        validateUser(user);
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", user.getFullName());
        vars.put("frontendUrl", frontendUrl);
        sendTemplatedEmail(user.getEmail(), "Willkommen bei Blasmusik Eisenstadt", "emails/welcome-email", vars);
    }

    @Async
    @Override
    public void sendVerificationEmail(User user, String verificationToken) {
        validateUserAndToken(user, verificationToken);
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", user.getFullName());
        vars.put("verificationUrl", frontendUrl + "/verify-email?token=" + verificationToken);
        sendTemplatedEmail(user.getEmail(), "Bitte bestätigen Sie Ihre E-Mail-Adresse", "emails/verification-email", vars);
    }

    @Async
    @Override
    public void sendOtpEmail(User user, String otpCode) {
        validateUserAndToken(user, otpCode);
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", user.getFullName());
        vars.put("otpCode", otpCode);
        sendTemplatedEmail(user.getEmail(), "Ihr Einmalpasswort", "emails/otp-email", vars);
    }

    @Async
    @Override
    public void sendPasswordResetEmail(User user, String resetToken) {
        validateUserAndToken(user, resetToken);
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", user.getFullName());
        vars.put("resetUrl", frontendUrl + "/reset-password?token=" + resetToken);
        sendTemplatedEmail(user.getEmail(), "Passwort zurücksetzen", "emails/password-reset-email", vars);
    }

    @Async
    @Override
    public void sendAccountLockedEmail(User user) {
        validateUser(user);
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", user.getFullName());
        sendTemplatedEmail(user.getEmail(), "Konto gesperrt", "emails/account-locked-email", vars);
    }

    @Async
    @Override
    public void sendAccountUnlockedEmail(User user) {
        validateUser(user);
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", user.getFullName());
        sendTemplatedEmail(user.getEmail(), "Konto entsperrt", "emails/account-unlocked-email", vars);
    }

    @Async
    @Override
    public void sendNewLoginAlert(User user, String deviceInfo, String locationInfo) {
        validateUser(user);
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", user.getFullName());
        vars.put("deviceInfo", deviceInfo != null ? deviceInfo : "Unbekanntes Gerät");
        vars.put("locationInfo", locationInfo != null ? locationInfo : "Unbekannter Standort");
        vars.put("loginTime", DATE_TIME_FORMATTER.format(java.time.LocalDateTime.now()));
        sendTemplatedEmail(user.getEmail(), "Neue Anmeldung erkannt", "emails/new-login-alert-email", vars);
    }

    @Async
    @Override
    public void sendEventNotification(User user, String eventTitle, String eventDate, String eventLocation, String additionalInfo) {
        validateUser(user);
        if (eventTitle == null || eventTitle.isBlank() || eventDate == null || eventDate.isBlank()) {
            throw new IllegalArgumentException("Event title and date must not be null or empty");
        }
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", user.getFullName());
        vars.put("eventTitle", eventTitle);
        vars.put("eventDate", eventDate);
        vars.put("eventLocation", eventLocation != null ? eventLocation : "Nicht angegeben");
        vars.put("additionalInfo", additionalInfo != null ? additionalInfo : "");
        sendTemplatedEmail(user.getEmail(), "Anstehende Veranstaltung: " + eventTitle, "emails/event-notification-email", vars);
    }

    @Async
    @Override
    public void sendSystemAlert(String subject, String message, String errorDetails) {
        if (adminEmail == null || adminEmail.isBlank()) {
            log.warn("Admin email nicht konfiguriert, Systemalert wird nicht versendet");
            return;
        }
        Map<String, Object> vars = new HashMap<>();
        vars.put("subject", subject);
        vars.put("message", message);
        vars.put("errorDetails", errorDetails != null ? errorDetails : "Keine Details verfügbar");
        vars.put("timestamp", DATE_TIME_FORMATTER.format(java.time.LocalDateTime.now()));
        sendTemplatedEmail(adminEmail, "[System Alert] " + subject, "emails/system-alert-email", vars);
    }

    @Override
    public void sendEmail(String to, String subject, String content, boolean isHtml) {
        if (to == null || to.isBlank() || subject == null || subject.isBlank() || content == null || content.isBlank()) {
            throw new IllegalArgumentException("Email Parameter dürfen nicht null oder leer sein");
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, isHtml);
            mailSender.send(message);
            log.debug("Email gesendet an {}", to);
        } catch (MessagingException e) {
            log.error("Fehler beim Senden der Email an {}", to, e);
            throw new RuntimeException("Email senden fehlgeschlagen", e);
        }
    }

    // --- Hilfsmethoden ---

    private void validateUser(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("User und User Email dürfen nicht null oder leer sein");
        }
    }

    private void validateUserAndToken(User user, String token) {
        validateUser(user);
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token darf nicht null oder leer sein");
        }
    }

    private void sendTemplatedEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            Context ctx = new Context(Locale.GERMAN);
            variables.forEach(ctx::setVariable);
            String content = templateEngine.process(templateName, ctx);
            sendEmail(to, subject, content, true);
            log.info("Email gesendet an {} mit Betreff {}", to, subject);
        } catch (Exception e) {
            log.error("Fehler beim Verarbeiten der Email Vorlage {} für {}", templateName, to, e);
            throw new RuntimeException("Email Vorlage Verarbeitung fehlgeschlagen", e);
        }
    }
}
