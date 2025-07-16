package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.Year;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Override
    public void sendWelcomeEmail(String email, String emailTemplate, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.addAttachment("Blasmusik_Logo.png", new ClassPathResource("static/images/Blasmusik_Logo.png"));
            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("year", Year.now().getValue());

            String html = templateEngine.process("emails/welcome-email", context);
            helper.setTo(email);
            helper.setText(html, true);
            helper.setSubject("Willkommen bei Stadtkapelle Eisenstadt");
            helper.setFrom(fromEmail);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
