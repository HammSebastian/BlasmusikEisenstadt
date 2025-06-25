package at.sebastianhamm.kapelle_eisenstadt.dto;

import at.sebastianhamm.kapelle_eisenstadt.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;
    
    @Builder.Default
    @NotNull(message = "Role cannot be null")
    private Role role = Role.USER;
    
    // Helper method to create a RegisterRequest with default USER role
    public static RegisterRequest withDefaultRole(RegisterRequest request) {
        if (request == null) {
            return null;
        }
        return RegisterRequest.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .build();
    }
}
