package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {

    private String email;
    private String role;
    private boolean emailNotification;
    private String avatarUrl;
    private String firstName;
    private String lastName;
}
