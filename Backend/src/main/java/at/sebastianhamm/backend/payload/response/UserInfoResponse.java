package at.sebastianhamm.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO for detailed user info response including tokens.
 * Tokens should be securely handled and only returned over HTTPS.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private Long id;

    private String username;

    private String email;

    private List<String> roles;

    private String refreshToken;

    private String token;
}
