package at.sebastianhamm.backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO representing an email with template support.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    private String from;

    private String to;

    private String subject;

    private HtmlTemplate htmlTemplate;
}
