package at.sebastianhamm.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * DTO for user profile responses.
 * Only exposes safe fields; roles as strings.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private Long id;

    private String username;

    private String email;

    private Set<String> roles;
}
