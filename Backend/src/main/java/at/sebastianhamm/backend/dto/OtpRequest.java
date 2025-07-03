package at.sebastianhamm.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO für OTP-Verifizierung.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequest {

    @NotBlank(message = "Email darf nicht leer sein")
    @Email(message = "Ungültige Email-Adresse")
    private String email;

    @NotBlank(message = "OTP darf nicht leer sein")
    private String otp;
}
