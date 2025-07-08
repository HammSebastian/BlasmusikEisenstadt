package at.sebastianhamm.backend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for incoming HeroItem creation/update requests.
 * Validated for non-blank fields to prevent invalid data.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeroItemRequest {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotBlank(message = "Image URL must not be blank")
    private String imageUrl;
}
