package at.sebastianhamm.backend.controller;


import at.sebastianhamm.backend.models.About;
import at.sebastianhamm.backend.payload.response.AboutResponse;
import at.sebastianhamm.backend.services.AboutService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/public/about")
@Tag(name = "About", description = "The about endpoint")
@RequiredArgsConstructor
public class AboutController {

    private final AboutService aboutService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AboutResponse getAbout() {
        return aboutService.getAbout();
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_REPORTER')")
    @ResponseStatus(HttpStatus.OK)
    public AboutResponse updateAbout(@Valid @RequestBody About about) {
        return aboutService.updateAbout(about);
    }
}
