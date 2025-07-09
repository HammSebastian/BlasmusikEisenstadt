package at.sebastianhamm.backend.models.common;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Holds information about an HTML template for various purposes, typically email generation.
 * It includes the template name and a map of properties to be injected into the template.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class HtmlTemplate {

    /**
     * Name or path of the template (e.g., Thymeleaf template name without suffix and prefix).
     */
    private final String template;

    /**
     * Map of variables (key-value pairs) to be injected into the template.
     */
    private final Map<String, Object> props;
}