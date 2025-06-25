package at.sebastianhamm.kapelle_eisenstadt.controller;

import at.sebastianhamm.kapelle_eisenstadt.dto.AuthenticationRequest;
import at.sebastianhamm.kapelle_eisenstadt.dto.AuthenticationResponse;
import at.sebastianhamm.kapelle_eisenstadt.dto.RegisterRequest;
import at.sebastianhamm.kapelle_eisenstadt.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        // Ensure default role is set if not provided
        RegisterRequest requestWithDefaultRole = RegisterRequest.withDefaultRole(request);
        return ResponseEntity.ok(authenticationService.register(requestWithDefaultRole));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
