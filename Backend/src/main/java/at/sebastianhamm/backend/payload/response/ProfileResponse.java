package at.sebastianhamm.backend.payload.response;

import at.sebastianhamm.backend.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;


@Data
@AllArgsConstructor
public class ProfileResponse {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
}
