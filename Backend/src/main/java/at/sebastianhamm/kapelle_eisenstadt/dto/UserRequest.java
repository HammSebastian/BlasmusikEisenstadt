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

/**
 * DTO for creating or updating a user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private Long id;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;
    
    @NotNull(message = "Role cannot be null")
    private Role role;
    
    /**
     * Creates a new UserRequest with default USER role if role is not specified
     * @param request the original UserRequest
     * @return a new UserRequest with default role if needed
     */
    public static UserRequest withDefaultRole(UserRequest request) {
        if (request == null) {
            return null;
        }
        return UserRequest.builder()
                .id(request.getId())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .build();
    }
}
