package at.sebastianhamm.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.StandardCharsets;

/**
 * Configuration for email templates using Thymeleaf.
 */
@Configuration
public class EmailTemplateConfig {

    private final EmailConfig emailConfig;

    public EmailTemplateConfig(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    /**
     * Configures the Thymeleaf template engine for email templates.
     *
     * @return the configured SpringTemplateEngine instance
     */
    @Bean
    public SpringTemplateEngine emailTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(htmlTemplateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    /**
     * Configures the template resolver for HTML email templates.
     *
     * @return the configured ITemplateResolver instance
     */
    private ITemplateResolver htmlTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(emailConfig.getTemplates().getPrefix());
        templateResolver.setSuffix(emailConfig.getTemplates().getSuffix());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setCacheable(emailConfig.getTemplates().isCache());
        templateResolver.setOrder(1);
        return templateResolver;
    }

    /**
     * Configures the template resolver for plain text email templates.
     *
     * @return the configured ITemplateResolver instance for plain text
     */
    @Bean
    public ITemplateResolver textTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(emailConfig.getTemplates().getPrefix());
        templateResolver.setSuffix(".txt");
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setCacheable(emailConfig.getTemplates().isCache());
        templateResolver.setOrder(2);
        return templateResolver;
    }
}
