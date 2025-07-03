package at.sebastianhamm.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for email settings.
 * These properties are loaded from application.yml/application.properties
 * with the prefix 'app.email'.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.email")
public class EmailConfig {

    /**
     * The 'from' email address used for all outgoing emails.
     */
    private String from;

    /**
     * The support email address where users can contact for help.
     */
    private String supportEmail;

    /**
     * The admin email address where system alerts are sent.
     */
    private String adminEmail;

    /**
     * The base URL for the frontend application, used for generating links in emails.
     */
    private String frontendBaseUrl;

    /**
     * Whether email sending is enabled.
     * If false, emails will be logged instead of being sent.
     */
    private boolean enabled = true;

    /**
     * The number of minutes an OTP code is valid.
     */
    private Integer otpExpiryMinutes = 10;

    /**
     * The number of hours a password reset token is valid.
     */
    private Integer passwordResetExpiryHours = 24;

    /**
     * The number of minutes an account remains locked after too many failed login attempts.
     */
    private Integer accountLockoutMinutes = 30;

    /**
     * The maximum number of failed login attempts before account is locked.
     */
    private Integer maxLoginAttempts = 5;

    /**
     * Configuration for the email templates.
     */
    private final TemplateConfig templates = new TemplateConfig();

    /**
     * Configuration for the SMTP server.
     */
    private final SmtpConfig smtp = new SmtpConfig();

    /**
     * Configuration for email templates.
     */
    @Data
    public static class TemplateConfig {
        private String prefix = "classpath:/templates/";
        private String suffix = ".html";
        private String encoding = "UTF-8";
        private boolean cache = true;
    }

    /**
     * Configuration for the SMTP server.
     */
    @Data
    public static class SmtpConfig {
        private String host;
        private Integer port = 587;
        private String username;
        private String password;
        private String protocol = "smtp";
        private boolean tls = true;
        private boolean auth = true;
        private final Map<String, String> properties = new HashMap<>();
    }
}
