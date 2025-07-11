package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.exceptions.NotFoundException;
import at.sebastianhamm.backend.payload.request.GigRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.GigResponse;
import at.sebastianhamm.backend.service.GigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gigs")
@RequiredArgsConstructor
public class GigController {

    private static final Logger logger = LoggerFactory.getLogger(GigController.class);
    private final GigService gigService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GigResponse>>> getAllGigs() {
        logger.info("Fetching all gigs");

        if (gigService.getAllGigs().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "No gigs found", null));
        }

        List<GigResponse> gigs = gigService.getAllGigs();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Gigs fetched successfully", gigs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GigResponse>> getGigById(@PathVariable Long id) {
        logger.info("Fetching gig with id {}", id);

        if (gigService.getGigById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Gig not found", null));
        }
        GigResponse gig = gigService.getGigById(id)
                .orElseThrow(() -> new NotFoundException("Gig not found with id: " + id));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Gig fetched successfully", gig));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('REPORTER')")
    public ResponseEntity<ApiResponse<GigResponse>> createGig(@Valid @RequestBody GigRequest gig) {
        logger.info("Creating new gig");

        if (gig.getLocationId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Location must be set", null));
        }
        ApiResponse<GigResponse> created = gigService.createGig(gig);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('REPORTER')")
    public ResponseEntity<ApiResponse<GigResponse>> updateGig(@PathVariable Long id, @Valid @RequestBody GigRequest gig) {
        logger.info("Updating gig with id {}", id);

        if (gigService.getGigById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Gig not found", null));
        }

        if (gig.getLocationId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Location must be set", null));
        }
        ApiResponse<GigResponse> updated = gigService.updateGig(id, gig);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('REPORTER')")
    public ResponseEntity<ApiResponse<Void>> deleteGig(@PathVariable Long id) {
        logger.info("Deleting gig with id {}", id);

        if (gigService.getGigById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Gig not found", null));
        }
        gigService.deleteGigById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Gig deleted successfully", null));
    }
}
