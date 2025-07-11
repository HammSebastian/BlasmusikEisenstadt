package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.exceptions.NotFoundException;
import at.sebastianhamm.backend.payload.request.RehearsalRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.RehearsalResponse;
import at.sebastianhamm.backend.service.RehearsalService;
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
@RequestMapping("/rehearsals")
@RequiredArgsConstructor
public class RehearsalController {

    private final RehearsalService rehearsalService;
    private static final Logger logger = LoggerFactory.getLogger(RehearsalController.class);

    @GetMapping
    public ResponseEntity<ApiResponse<List<RehearsalResponse>>> getAllRehearsals() {
        logger.info("Fetching all rehearsals");

        if (rehearsalService.getAllRehearsals().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "No rehearsals found", null));
        }
        List<RehearsalResponse> rehearsals = rehearsalService.getAllRehearsals();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Rehearsals fetched successfully", rehearsals));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RehearsalResponse>> getRehearsalById(@PathVariable Long id) {
        logger.info("Fetching rehearsal with id {}", id);

        if (rehearsalService.getRehearsalById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Rehearsal not found", null));
        }

        RehearsalResponse rehearsal = rehearsalService.getRehearsalById(id)
                .orElseThrow(() -> new NotFoundException("Rehearsal not found with id: " + id));

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Rehearsal fetched successfully", rehearsal));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPORTER')")
    public ResponseEntity<ApiResponse<RehearsalResponse>> createRehearsal(@Valid @RequestBody RehearsalRequest request) {
        logger.info("Creating new rehearsal");

        if (request.getLocationId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Location ID must be provided", null));
        }
        ApiResponse<RehearsalResponse> created = rehearsalService.createRehearsal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPORTER')")
    public ResponseEntity<ApiResponse<RehearsalResponse>> updateRehearsal(@PathVariable Long id,
                                                                          @Valid @RequestBody RehearsalRequest request) {
        logger.info("Updating rehearsal with id {}", id);

        if (rehearsalService.getRehearsalById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Rehearsal not found", null));
        }

        return ResponseEntity.ok(rehearsalService.updateRehearsal(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPORTER')")
    public ResponseEntity<ApiResponse<Void>> deleteRehearsal(@PathVariable Long id) {
        logger.info("Deleting rehearsal with id {}", id);

        if (rehearsalService.getRehearsalById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Rehearsal not found", null));
        }

        if (rehearsalService.getRehearsalById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Rehearsal not found", null));
        }
        rehearsalService.deleteRehearsal(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Rehearsal deleted successfully", null));
    }
}
