package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.io.ProfileResponse;
import at.sebastianhamm.backend.service.EmailService;
import at.sebastianhamm.backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final EmailService emailService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        if (email == null) {
            throw new IllegalArgumentException("User not authenticated");
        }
        return profileService.getProfile(email);
    }

    // Optional: Methode zum Update des Profils (PUT/PATCH) hier ergänzen

    // Beispiel Async-Mail-Versand im Service:
    // emailService.sendWelcomeEmailAsync(email, name);
}
