package at.sebastianhamm.backend.service;

import at.sebastianhamm.backend.entity.Gig;
import at.sebastianhamm.backend.entity.Rehearsal;
import at.sebastianhamm.backend.payload.request.ParticipationRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.ParticipationResponse;

import java.util.List;
import java.util.Optional;

public interface ParticipationService {

    Optional<ParticipationResponse> getParticipationByRehearsalId(Long rehearsalId);
    Optional<ParticipationResponse> getParticipationByGigId(Long gigId);
    ApiResponse<ParticipationResponse> createParticipation(ParticipationRequest request);
    List<ParticipationResponse> findAllByRehearsal(Rehearsal rehearsal);
    List<ParticipationResponse> findAllByGig(Gig gig);
    boolean deleteParticipation(Long id);


}
