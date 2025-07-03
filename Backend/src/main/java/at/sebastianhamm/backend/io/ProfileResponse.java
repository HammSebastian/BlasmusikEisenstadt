package at.sebastianhamm.backend.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
    private String userId;
    private String name;
    private String email;
    private boolean accountVerified; // primitive boolean, nicht Boolean
}
