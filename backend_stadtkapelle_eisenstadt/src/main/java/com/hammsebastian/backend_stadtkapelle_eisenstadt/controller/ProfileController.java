package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ProfileResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        return ResponseEntity.ok().body(
                ApiResponse.<ProfileResponse>builder()
                        .message("Profile retrieved successfully")
                        .statusCode(HttpStatus.OK.value())
                        .data(profileService.getProfile(email))
                        .build()
        );
    }
}
