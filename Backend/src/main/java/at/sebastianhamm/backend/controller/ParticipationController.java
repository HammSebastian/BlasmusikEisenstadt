package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.payload.request.ParticipationRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.ParticipationResponse;
import at.sebastianhamm.backend.repository.GigRepository;
import at.sebastianhamm.backend.repository.RehearsalRepository;
import at.sebastianhamm.backend.service.ParticipationService;
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
@RequestMapping("/participations")
@RequiredArgsConstructor
public class ParticipationController {
    private final ParticipationService participationService;
    private static final Logger logger = LoggerFactory.getLogger(ParticipationController.class);
    private final RehearsalRepository rehearsalRepository;
    private final GigRepository gigRepository;

    @GetMapping("/rehearsal/{rehearsalId}")
    public ResponseEntity<ApiResponse<ParticipationResponse>> getParticipationByRehearsalId(@PathVariable Long rehearsalId) {
        logger.info("Fetching participation for rehearsal with id: {}", rehearsalId);

        return participationService.getParticipationByRehearsalId(rehearsalId)
                .map(participation -> ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Participation found", participation)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Participation not found", null)));
    }

    @GetMapping("/gig/{gigId}")
    public ResponseEntity<ApiResponse<ParticipationResponse>> getParticipationByGigId(@PathVariable Long gigId) {
        logger.info("Fetching participation for gig with id: {}", gigId);

        return participationService.getParticipationByGigId(gigId)
                .map(participation -> ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Participation found", participation)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Participation not found", null)));
    }

    @GetMapping("/admin/rehearsal/{rehearsalId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('REPORTER') || hasRole('CONDUCTOR') || hasRole('SECTION_LEADER')")
    public ResponseEntity<ApiResponse<List<ParticipationResponse>>> getAllByRehearsal(@PathVariable Long rehearsalId) {
        logger.info("Fetching all participations for rehearsal with id: {}", rehearsalId);
        List<ParticipationResponse> participations = participationService.findAllByRehearsal(rehearsalRepository.getById(rehearsalId));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Participations fetched", participations));
    }

    @GetMapping("/admin/gig/{gigId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ParticipationResponse>>> getAllByGig(@PathVariable Long gigId) {
        logger.info("Fetching all participations for gig with id: {}", gigId);
        List<ParticipationResponse> participations = participationService.findAllByGig(gigRepository.getById(gigId));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Participations fetched", participations));
    }



    @PostMapping
    public ResponseEntity<ApiResponse<ParticipationResponse>> createParticipation(@Valid @RequestBody ParticipationRequest request) {
        logger.info("Creating participation for user with request: {}", request);

        if (request.getGigId() == null && request.getRehearsalId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Either gigId or rehearsalId must be provided", null));
        }

        ApiResponse<ParticipationResponse> response = participationService.createParticipation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteParticipation(@PathVariable Long id) {
        logger.info("Deleting participation with id: {}", id);

        if (participationService.deleteParticipation(id)) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Participation deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Participation not found", null));
        }
    }
}
