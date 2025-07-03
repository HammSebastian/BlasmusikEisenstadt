package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.io.GigRequest;
import at.sebastianhamm.backend.io.GigResponse;
import at.sebastianhamm.backend.service.GigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gigs")
public class GigController {

    private static final Logger logger = LoggerFactory.getLogger(GigController.class);
    private final GigService gigService;

    @GetMapping
    public List<GigResponse> getAllGigs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (location != null) {
            logger.info("Fetching gigs by location '{}'", location);
            return gigService.getGigsByLocation(location);
        } else if (date != null) {
            logger.info("Fetching gigs by date '{}'", date);
            return gigService.getGigsByDate(date);
        } else {
            logger.info("Fetching all gigs");
            return gigService.getAllGigs();
        }
    }

    @GetMapping("/{id}")
    public GigResponse getGig(@PathVariable Long id) {
        logger.info("Fetching gig with id {}", id);
        return gigService.getGigById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GigResponse createGig(@Valid @RequestBody GigRequest gig) {
        logger.info("Creating new gig: {}", gig);
        return gigService.createGig(gig);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateGig(@PathVariable Long id, @Valid @RequestBody GigRequest gig) {
        logger.info("Updating gig with id {}: {}", id, gig);
        gigService.updateGig(id, gig);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGig(@PathVariable Long id) {
        logger.info("Deleting gig with id {}", id);
        gigService.deleteGigById(id);
    }
}
