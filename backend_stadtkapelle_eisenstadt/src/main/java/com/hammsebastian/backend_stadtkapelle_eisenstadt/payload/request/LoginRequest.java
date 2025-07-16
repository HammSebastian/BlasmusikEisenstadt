package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @Column(unique = true, nullable = false)
    @NotNull(message = "Email is required")
    @Email
    private String email;

    @Column(nullable = false)
    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Size(max = 265, message = "Password must be at most 265 characters long")
    private String password;


    private String otp;
}
