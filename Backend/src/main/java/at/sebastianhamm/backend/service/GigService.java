package at.sebastianhamm.backend.service;

import at.sebastianhamm.backend.io.GigRequest;
import at.sebastianhamm.backend.io.GigResponse;

import java.time.LocalDate;
import java.util.List;

public interface GigService {

    GigResponse createGig(GigRequest request);

    GigResponse getGigById(Long id);

    List<GigResponse> getAllGigs();

    List<GigResponse> getGigsByDate(LocalDate date);

    List<GigResponse> getGigsBetweenDates(LocalDate startDate, LocalDate endDate);

    GigResponse updateGig(Long id, GigRequest request);

    void deleteGigById(Long id);

    List<GigResponse> getGigsByLocation(String location);
}
