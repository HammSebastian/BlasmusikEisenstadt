package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.exception.ResourceNotFoundException;
import at.sebastianhamm.backend.models.gig.enums.EGigs;
import at.sebastianhamm.backend.models.gig.Gig;
import at.sebastianhamm.backend.payload.response.GigResponse;
import at.sebastianhamm.backend.services.GigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/public/gigs")
@RequiredArgsConstructor
public class GigController {

    private final GigService gigService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GigResponse> getAllGigs() {
        return gigService.getAllGigs();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GigResponse getGigById(@PathVariable Long id) {
        return gigService.getGigById(id)
                .map(gigService::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Gig not found with id: " + id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_REPORTER')")
    @ResponseStatus(HttpStatus.CREATED)
    public GigResponse createGig(@Valid @RequestBody Gig gig) {
        return gigService.createGig(gig);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_REPORTER')")
    @ResponseStatus(HttpStatus.OK)
    public GigResponse updateGig(@PathVariable Long id, @Valid @RequestBody Gig gig) {
        return gigService.updateGig(id, gig);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_REPORTER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGig(@PathVariable Long id) {
        gigService.deleteGig(id);
    }

    @GetMapping("/type/{type}")
    @ResponseStatus(HttpStatus.OK)
    public List<Gig> getGigsByType(@PathVariable EGigs type) {
        return gigService.findByType(type);
    }
}
