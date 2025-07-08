package at.sebastianhamm.backend.models;

import lombok.*;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Mail {

    private String from;
    private String to;
    private String subject;
    private HtmlTemplate htmlTemplate;
}
