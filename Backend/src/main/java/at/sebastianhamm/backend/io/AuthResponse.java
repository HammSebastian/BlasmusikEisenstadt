package at.sebastianhamm.backend.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private final String email;
    private final String jwtToken;
}
