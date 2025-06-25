package at.sebastianhamm.kapelle_eisenstadt.dto;

import at.sebastianhamm.kapelle_eisenstadt.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    
    // Note: Password is intentionally excluded from the response for security
}
