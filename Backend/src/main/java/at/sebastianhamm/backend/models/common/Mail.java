package at.sebastianhamm.backend.models.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Represents an email message with essential details like sender, recipient, subject,
 * and the HTML content template.
 */
@Getter
@Setter
@Builder
public class Mail {

    /**
     * Optional email address of the sender. If null or empty, a default sender
     * from configuration will be used.
     */
    private String from;

    /**
     * Email address of the primary recipient. Cannot be null.
     */
    @NonNull
    private String to;

    /**
     * Subject line of the email. Cannot be null.
     */
    @NonNull
    private String subject;

    /**
     * HTML template object containing the template name and its properties. Cannot be null.
     */
    @NonNull
    private HtmlTemplate htmlTemplate;
}