package at.sebastianhamm.kapelle_eisenstadt.dto;

import at.sebastianhamm.kapelle_eisenstadt.models.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
}
