package at.sebastianhamm.backend.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * DTO for user signup requests.
 * Validates username, email, password strength and roles.
 */
@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Email must not be blank")
    @Size(max = 50, message = "Email must be at most 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    private Set<String> role;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
            message = "Password must contain digit, lowercase, uppercase, special char, and no whitespace"
    )
    private String password;
}
