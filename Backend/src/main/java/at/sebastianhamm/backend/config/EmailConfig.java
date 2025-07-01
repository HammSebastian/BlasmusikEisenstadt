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
    private int otpExpiryMinutes = 10;
    
    /**
     * The number of hours a password reset token is valid.
     */
    private int passwordResetExpiryHours = 24;
    
    /**
     * The number of minutes an account remains locked after too many failed login attempts.
     */
    private int accountLockoutMinutes = 30;
    
    /**
     * The maximum number of failed login attempts before account is locked.
     */
    private int maxLoginAttempts = 5;
    
    /**
     * Configuration for the email templates.
     */
    private TemplateConfig templates = new TemplateConfig();
    
    /**
     * Configuration for email templates.
     */
    @Data
    public static class TemplateConfig {
        /**
         * The prefix for template locations.
         */
        private String prefix = "classpath:/templates/";
        
        /**
         * The suffix for template files.
         */
        private String suffix = ".html";
        
        /**
         * The encoding for template files.
         */
        private String encoding = "UTF-8";
        
        /**
         * Whether template caching is enabled.
         */
        private boolean cache = true;
    }
    
    /**
     * Configuration for the SMTP server.
     */
    @Data
    public static class SmtpConfig {
        /**
         * The SMTP server host.
         */
        private String host;
        
        /**
         * The SMTP server port.
         */
        private int port = 587;
        
        /**
         * The username for SMTP authentication.
         */
        private String username;
        
        /**
         * The password for SMTP authentication.
         */
        private String password;
        
        /**
         * The protocol to use (smtp, smtps).
         */
        private String protocol = "smtp";
        
        /**
         * Whether to enable TLS.
         */
        private boolean tls = true;
        
        /**
         * Whether to authenticate with the SMTP server.
         */
        private boolean auth = true;
        
        /**
         * Additional JavaMail properties.
         */
        private Map<String, String> properties = new HashMap<>();
    }
    
    /**
     * Gets the SMTP configuration.
     */
    private SmtpConfig smtp = new SmtpConfig();
}
