package at.sebastianhamm.backend.service.impl;

import at.sebastianhamm.backend.entity.Gig;
import at.sebastianhamm.backend.entity.Participation;
import at.sebastianhamm.backend.entity.Rehearsal;
import at.sebastianhamm.backend.entity.User;
import at.sebastianhamm.backend.enums.ParticipationStatus;
import at.sebastianhamm.backend.exceptions.BadRequestException;
import at.sebastianhamm.backend.payload.request.ParticipationRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.ParticipationResponse;
import at.sebastianhamm.backend.repository.GigRepository;
import at.sebastianhamm.backend.repository.ParticipationRepository;
import at.sebastianhamm.backend.repository.RehearsalRepository;
import at.sebastianhamm.backend.repository.UserRepository;
import at.sebastianhamm.backend.service.ParticipationService;
import at.sebastianhamm.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final GigRepository gigRepository;
    private final RehearsalRepository rehearsalRepository;
    private final UserService userService;

    @Override
    public ApiResponse<ParticipationResponse> createParticipation(ParticipationRequest request) {
        // 1. Aktuellen User holen
        ApiResponse<User> userResponse = userService.getCurrentLoggedInUser();
        if (userResponse.getStatusCode() != 200 || userResponse.getData() == null) {
            return new ApiResponse<>(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Current user not found or unauthorized",
                    null
            );
        }

        User currentUser = userResponse.getData();

        // 2. Entweder Gig oder Rehearsal muss gesetzt sein, aber nicht beides
        boolean hasGig = request.getGigId() != null;
        boolean hasRehearsal = request.getRehearsalId() != null;

        if (hasGig == hasRehearsal) {
            return new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Either gigId or rehearsalId must be provided, not both or none",
                    null
            );
        }

        // 3. Status muss gesetzt sein
        if (request.getStatus() == null) {
            return new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Participation status must be provided",
                    null
            );
        }

        // 4. Bei Absage oder Vielleicht ist ein Grund verpflichtend
        if ((request.getStatus() == ParticipationStatus.NO || request.getStatus() == ParticipationStatus.MAYBE)
                && (request.getReason() == null || request.getReason().isBlank())) {
            return new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Reason is required for status NO or MAYBE",
                    null
            );
        }

        // 5. Entity-Zuordnung vorbereiten
        Participation.ParticipationBuilder builder = Participation.builder()
                .user(currentUser)
                .status(request.getStatus())
                .reason(request.getReason())
                .respondedAt(LocalDateTime.now());

        if (hasGig) {
            Gig gig = gigRepository.findById(request.getGigId())
                    .orElseThrow(() -> new BadRequestException("Gig not found with id: " + request.getGigId()));
            builder.gig(gig);
        } else {
            Rehearsal rehearsal = rehearsalRepository.findById(request.getRehearsalId())
                    .orElseThrow(() -> new BadRequestException("Rehearsal not found with id: " + request.getRehearsalId()));
            builder.rehearsal(rehearsal);
        }

        // 6. Speichern und Response bauen
        Participation saved = participationRepository.save(builder.build());
        ParticipationResponse response = ParticipationResponse.fromParticipation(saved);

        return new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Participation created successfully",
                response
        );
    }



    @Override
    public boolean deleteParticipation(Long id) {
        if (participationRepository.existsById(id)) {
            participationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<ParticipationResponse> getParticipationByRehearsalId(Long rehearsalId) {

        if (rehearsalId == null) {
            throw new BadRequestException("Rehearsal ID must be provided");
        }

        Rehearsal rehearsal = rehearsalRepository.findById(rehearsalId)
                .orElseThrow(() -> new BadRequestException("Rehearsal not found with id: " + rehearsalId));

        return participationRepository.findParticipationByRehearsal(rehearsal)
                .map(ParticipationResponse::fromParticipation);
    }

    @Override
    public Optional<ParticipationResponse> getParticipationByGigId(Long gigId) {
        if (gigId == null) {
            throw new BadRequestException("Gig ID must be provided");
        }

        Gig gig = gigRepository.findById(gigId)
                .orElseThrow(() -> new BadRequestException("Gig not found with id: " + gigId));

        return participationRepository.findParticipationByGig(gig)
                .map(ParticipationResponse::fromParticipation);
    }
}
