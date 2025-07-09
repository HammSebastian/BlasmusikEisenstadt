package at.sebastianhamm.backend.services;

import at.sebastianhamm.backend.models.common.Mail;

/**
 * Service Interface to send emails and process email templates.
 */
public interface EmailService {
    /**
     * Send a welcome email using the provided Mail object.
     * @param mail mail data including recipient, subject and template.
     */
    void sendWelcomeEmail(Mail mail);
}
