package at.sebastianhamm.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * DTO representing an HTML template with properties.
 * Immutable structure for template rendering.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HtmlTemplate {
    private String template;
    private Map<String, Object> props;
}
