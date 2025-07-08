package at.sebastianhamm.backend.services.impl;

import at.sebastianhamm.backend.models.Mail;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.from}")
    private String from;

    @Override
    public void sendWelcomeEmail(Mail mail){
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            String html = getHTMLContent(mail);

            helper.setTo(mail.getTo());
            helper.setFrom(from);
            helper.setSubject(mail.getSubject());
            helper.setText(html, true);

            log.info("Sende E-Mail von: {}", mail.getFrom());
            log.info("SMTP Benutzername: {}", emailSender);

            emailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error sending email", e);
        }
    }

    @Override
    public String getHTMLContent(Mail mail) {
        Context context = new Context();
        context.setVariables(mail.getHtmlTemplate().getProps());
        return templateEngine.process(mail.getHtmlTemplate().getTemplate(), context);
    }
}
