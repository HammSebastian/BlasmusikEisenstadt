package at.sebastianhamm.backend.service;

import at.sebastianhamm.backend.io.GigRequest;
import at.sebastianhamm.backend.io.GigResponse;

import java.util.Date;
import java.util.List;

public interface GigService {

    GigResponse createGig(GigRequest gigRequest);

    GigResponse getGig(String title);

    void deleteGig(String title);

    GigResponse updateGig(GigRequest gigRequest);

    List<GigResponse> getAllGigs();

    List<GigResponse> getGigsByLocation(String location);

    List<GigResponse> getGigsByDate(String date);
}
