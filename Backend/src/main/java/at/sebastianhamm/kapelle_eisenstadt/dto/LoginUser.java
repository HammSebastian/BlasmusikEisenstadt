package at.sebastianhamm.kapelle_eisenstadt.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginUser {
    private String username;
    private String password;
}
