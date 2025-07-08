package at.sebastianhamm.backend.services;

import at.sebastianhamm.backend.models.EGigs;
import at.sebastianhamm.backend.models.Gig;
import at.sebastianhamm.backend.payload.response.GigResponse;

import java.util.List;
import java.util.Optional;

public interface GigService {

    List<GigResponse> getAllGigs();

    Optional<Gig> getGigById(Long id);
    GigResponse createGig(Gig gig);
    GigResponse updateGig(Long id, Gig gig);
    void deleteGig(Long id);
    List<Gig> findByType(EGigs type);
    GigResponse mapToResponse(Gig gig);
}
