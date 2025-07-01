package at.sebastianhamm.backend.service.impl;

import at.sebastianhamm.backend.BackendApplication;
import at.sebastianhamm.backend.entity.GigEntity;
import at.sebastianhamm.backend.exception.ConflictException;
import at.sebastianhamm.backend.io.GigRequest;
import at.sebastianhamm.backend.io.GigResponse;
import at.sebastianhamm.backend.repository.GigRepository;
import at.sebastianhamm.backend.service.GigService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GigServiceImpl implements GigService {

    private final GigRepository gigRepository;

    private final Logger logger = BackendApplication.getLogger();

    @Override
    public GigResponse createGig(GigRequest gigRequest) {
        if (gigRepository.existsByTitleIgnoreCase(gigRequest.getTitle())) {
            logger.error("A gig with this title already exists (case-insensitive)");

            throw new ConflictException("A gig with this title already exists (case-insensitive)");
        }

        GigEntity newGig = convertToGigEntity(gigRequest);
        newGig = gigRepository.save(newGig);
        GigResponse response = convertToGigResponse(newGig);

        return response;
    }

    @Override
    public GigResponse getGig(String title) {
        GigEntity gig = gigRepository.findGigEntityByTitle(title)
                .orElseThrow(() -> new RuntimeException("Gig not found with title: " + title));
        return convertToGigResponse(gig);
    }

    @Override
    public void deleteGig(String title) {
        GigEntity gig = gigRepository.findGigEntityByTitle(title)
                .orElseThrow(() -> new RuntimeException("Gig not found with title: " + title));
        gigRepository.delete(gig);
    }

    @Override
    public GigResponse updateGig(GigRequest gigRequest) {
        GigEntity gig = gigRepository.findGigEntityByTitle(gigRequest.getTitle())
                .orElseThrow(() -> new RuntimeException("Gig not found with title: " + gigRequest.getTitle()));

        gig.setDescription(gigRequest.getDescription());
        gig.setLocation(gigRequest.getLocation());
        gig.setDate(gigRequest.getDate());
        gig.setStartTime(gigRequest.getStartTime());
        gig.setEndTime(gigRequest.getEndTime());

        gigRepository.save(gig);
        return convertToGigResponse(gig);
    }

    @Override
    public List<GigResponse> getAllGigs() {
        return gigRepository.findAll().stream()
                .map(this::convertToGigResponse)
                .toList();
    }

    @Override
    public List<GigResponse> getGigsByLocation(String location) {
        return gigRepository.findGigEntitiesByLocation(location).stream()
                .map(this::convertToGigResponse)
                .toList();
    }

    @Override
    public List<GigResponse> getGigsByDate(String date) {
        return gigRepository.findGigEntitiesByDate(date).stream()
                .map(this::convertToGigResponse)
                .toList();
    }

    private GigEntity convertToGigEntity(GigRequest gigRequest) {
        if (gigRequest == null) {
            System.out.println("Error: GigRequest is null");
            return null;
        }

        System.out.println("Converting GigRequest to GigEntity");
        System.out.println("Title: " + gigRequest.getTitle());
        System.out.println("Description: " + gigRequest.getDescription());
        System.out.println("Location: " + gigRequest.getLocation());
        System.out.println("Date: " + gigRequest.getDate());
        System.out.println("Start Time: " + gigRequest.getStartTime());
        System.out.println("End Time: " + gigRequest.getEndTime());

        try {
            GigEntity entity = GigEntity.builder()
                    .title(gigRequest.getTitle())
                    .description(gigRequest.getDescription())
                    .location(gigRequest.getLocation())
                    .date(gigRequest.getDate())
                    .startTime(gigRequest.getStartTime())
                    .endTime(gigRequest.getEndTime())
                    .build();
            System.out.println("Successfully created GigEntity");
            return entity;
        } catch (Exception e) {
            System.out.println("Error creating GigEntity: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    private GigResponse convertToGigResponse(GigEntity gig) {
        if (gig == null) {
            return null;
        }

        // Format time as "HH:mm - HH:mm" if both start and end times are present
        String time = gig.getStartTime();
        if (gig.getStartTime() != null && gig.getEndTime() != null) {
            time = gig.getStartTime() + " - " + gig.getEndTime();
        }

        return GigResponse.builder()
                .id(gig.getId())
                .title(gig.getTitle())
                .description(gig.getDescription())
                .location(gig.getLocation())
                .date(gig.getDate())
                .time(time)
                .build();
    }
}
