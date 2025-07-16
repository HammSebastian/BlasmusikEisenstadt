package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @Column(nullable = false)
    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Column(nullable = false)
    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Size(max = 265, message = "Password must be at most 265 characters long")

    /**
     * Configuration for password validation
     *
     * (?=.*[A-Z])      //at least one upper case letter
     * (?=.*[a-z])      //at least one lower case letter
     * (?=.*[0-9])      //at least one digit
     * (?=.*[!@#$%^&*]) //at least one special character
     * (?=.{8,})        //at least 8 characters
     */
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
            message = "Password must contain at least one upper case letter, one lower case letter, one digit, one special character, and be at least 8 characters long"
    )

    private String password;

    @Column(nullable = false)
    @NotNull(message = "Email notification is required")
    private boolean emailNotification;
}
