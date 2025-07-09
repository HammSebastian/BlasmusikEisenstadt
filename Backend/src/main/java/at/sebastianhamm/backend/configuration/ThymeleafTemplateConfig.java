package at.sebastianhamm.backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;

/**
 * Konfiguriert den Thymeleaf Template Engine für HTML-Templates.
 * Unterstützt dynamisches Caching via Spring-Konfiguration für unterschiedliche Umgebungen (z. B. dev vs. prod).
 */
@Configuration
public class ThymeleafTemplateConfig {

    /**
     * Initialisiert den {@link SpringTemplateEngine} und fügt den HTML-Resolver hinzu.
     *
     * @param htmlTemplateResolver Ein konfigurierter {@link ClassLoaderTemplateResolver}
     * @return Eine vollständig konfigurierte {@link SpringTemplateEngine} Instanz
     */
    @Bean
    public SpringTemplateEngine springTemplateEngine(ClassLoaderTemplateResolver htmlTemplateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver);
        return templateEngine;
    }

    /**
     * Konfiguriert einen TemplateResolver für HTML-Dateien im classpath unter /templates/.
     * Der Caching-Modus kann über die Property {@code spring.thymeleaf.cache} gesteuert werden.
     *
     * @param cacheable Gibt an, ob Templates gecached werden sollen (z. B. true in Production)
     * @return Ein konfigurierter {@link ClassLoaderTemplateResolver}
     */
    @Bean
    public ClassLoaderTemplateResolver htmlTemplateResolver(
            @Value("${spring.thymeleaf.cache:true}") boolean cacheable
    ) {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCacheable(cacheable);
        resolver.setOrder(1);
        return resolver;
    }
}
