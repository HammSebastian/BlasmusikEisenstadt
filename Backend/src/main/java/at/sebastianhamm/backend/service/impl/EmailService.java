package at.sebastianhamm.backend.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail, String name) {
        Context context = new Context();
        context.setVariable("name", name);
        String content = templateEngine.process("welcome-email", context);
        sendHtmlEmail(toEmail, "Welcome to our website", content);
    }

    public void sendOtpEmail(String toEmail, String otp) {
        Context context = new Context();
        context.setVariable("email", toEmail);
        context.setVariable("otp", otp);
        String content = templateEngine.process("verify-email", context);
        sendHtmlEmail(toEmail, "Account Verification OTP", content);
    }

    public void sendResetOtpEmail(String toEmail, String otp) {
        Context context = new Context();
        context.setVariable("email", toEmail);
        context.setVariable("otp", otp);
        String content = templateEngine.process("reset-password", context);
        sendHtmlEmail(toEmail, "Reset Password OTP", content);
    }

    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

