package at.sebastianhamm.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
public class HtmlTemplate {
    private String template;
    private Map<String, Object> props;
}
