package at.sebastianhamm.backend.io;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {

    @NotBlank(message = "Name is required")
    private String name;
    @Email(message = "Email is not valid")
    @NotNull(message = "Email is required")
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @NotNull(message = "Password is required")
    private String password;
}
