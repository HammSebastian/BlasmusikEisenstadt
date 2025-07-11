package at.sebastianhamm.backend.service;

import at.sebastianhamm.backend.payload.request.GigRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.GigResponse;

import java.util.List;
import java.util.Optional;

public interface GigService {

    ApiResponse<GigResponse> createGig(GigRequest gig);
    List<GigResponse> getAllGigs();
    Optional<GigResponse> getGigById(Long id);
    ApiResponse<GigResponse> updateGig(Long id, GigRequest updatedGig);
    boolean deleteGigById(Long id);
}
