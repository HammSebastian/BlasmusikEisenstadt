package at.sebastianhamm.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
public class AuthenticationRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    private String otp;

    // Standard-Konstruktor
    public AuthenticationRequest() {
    }

    // Konstruktor mit email und password
    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Konstruktor mit email, password und otp
    public AuthenticationRequest(String email, String password, String otp) {
        this.email = email;
        this.password = password;
        this.otp = otp;
    }

}
