package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.io.GigRequest;
import at.sebastianhamm.backend.io.GigResponse;
import at.sebastianhamm.backend.service.GigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/gigs")  // Einheitlicher Pfad
public class GigController {

    private final GigService gigService;

    @GetMapping
    public List<GigResponse> getAllGigs() {
        return gigService.getAllGigs();
    }

    @GetMapping("/location")
    public List<GigResponse> getGigsByLocation(@RequestParam String location) {
        return gigService.getGigsByLocation(location);
    }

    @GetMapping("/date")
    public List<GigResponse> getGigsByDate(@RequestParam String date) {
        return gigService.getGigsByDate(date);
    }

    @GetMapping("/title")
    public GigResponse getGig(@RequestParam String title) {
        return gigService.getGig(title);
    }

    @DeleteMapping
    public void deleteGig(@RequestParam String title) {
        gigService.deleteGig(title);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateGig(@Valid @RequestBody GigRequest gig) {
        gigService.updateGig(gig);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GigResponse createGig(@Valid @RequestBody GigRequest gig) {
        System.out.println("Received create gig request: " + gig);
        GigResponse response = gigService.createGig(gig);
        System.out.println("Sending response: " + response);
        return response;
    }
}

