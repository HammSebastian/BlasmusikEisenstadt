package at.sebastianhamm.backend.services.impl;

import at.sebastianhamm.backend.models.common.Mail;
import at.sebastianhamm.backend.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

/**
 * Implementation of EmailService to send emails with Thymeleaf HTML templates.
 * Uses JavaMailSender and SpringTemplateEngine for templating.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.from:no-reply@stadtkapelle-eisenstadt.at}")
    private String defaultFrom;

    /**
     * Sends a welcome email using the given Mail object.
     *
     * @param mail Mail object containing recipient, subject, from, and HTML template info.
     */
    @Override
    public void sendWelcomeEmail(Mail mail) {
        try {
            MimeMessage message = createMimeMessage(mail);
            emailSender.send(message);
            log.info("Welcome email sent to {}", mail.getTo());
        } catch (MessagingException e) {
            log.error("Failed to send welcome email to {}", mail.getTo(), e);
        }
    }

    /**
     * Creates a MimeMessage with HTML content based on the Mail object.
     *
     * @param mail Mail object containing email details and template.
     * @return MimeMessage ready to be sent.
     * @throws MessagingException if message creation fails.
     */
    private MimeMessage createMimeMessage(Mail mail) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        String htmlContent = generateHtmlContent(mail);

        helper.setTo(mail.getTo());
        helper.setFrom(mail.getFrom() != null && !mail.getFrom().isBlank() ? mail.getFrom() : defaultFrom);
        helper.setSubject(mail.getSubject());
        helper.setText(htmlContent, true);

        log.debug("Prepared email: to={}, from={}, subject={}", mail.getTo(), helper.getMimeMessage().getFrom(), mail.getSubject());
        return message;
    }

    /**
     * Processes the Thymeleaf HTML template with the variables from the Mail object.
     *
     * @param mail Mail object containing template name and properties.
     * @return Processed HTML content as String.
     */
    private String generateHtmlContent(Mail mail) {
        Context context = new Context();
        context.setVariables(mail.getHtmlTemplate().getProps());
        return templateEngine.process(mail.getHtmlTemplate().getTemplate(), context);
    }
}
