package at.sebastianhamm.backend.services;

import at.sebastianhamm.backend.models.Mail;

public interface EmailService {
    void sendWelcomeEmail(Mail mail);
    String getHTMLContent(Mail mail);
}
