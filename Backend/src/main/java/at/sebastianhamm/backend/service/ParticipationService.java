package at.sebastianhamm.backend.service;

import at.sebastianhamm.backend.payload.request.ParticipationRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.ParticipationResponse;

import java.util.Optional;

public interface ParticipationService {

    Optional<ParticipationResponse> getParticipationByRehearsalId(Long rehearsalId);
    Optional<ParticipationResponse> getParticipationByGigId(Long gigId);
    ApiResponse<ParticipationResponse> createParticipation(ParticipationRequest request);
    boolean deleteParticipation(Long id);
}
