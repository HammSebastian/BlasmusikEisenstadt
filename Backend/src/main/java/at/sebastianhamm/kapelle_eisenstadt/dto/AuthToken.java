package at.sebastianhamm.kapelle_eisenstadt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AuthToken {
    private String token;
    private String username;
}
