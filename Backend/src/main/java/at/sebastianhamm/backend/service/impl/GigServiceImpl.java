package at.sebastianhamm.backend.service.impl;

import at.sebastianhamm.backend.entity.Gig;
import at.sebastianhamm.backend.entity.Location;
import at.sebastianhamm.backend.entity.User;
import at.sebastianhamm.backend.exceptions.BadRequestException;
import at.sebastianhamm.backend.exceptions.NotFoundException;
import at.sebastianhamm.backend.payload.request.GigRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.GigResponse;
import at.sebastianhamm.backend.repository.GigRepository;
import at.sebastianhamm.backend.repository.LocationRepository;
import at.sebastianhamm.backend.service.GigService;
import at.sebastianhamm.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GigServiceImpl implements GigService {

    private final GigRepository gigRepository;
    private final LocationRepository locationRepository;
    private final UserService userService;

    @Override
    public List<GigResponse> getAllGigs() {
        List<Gig> gigs = gigRepository.findAll();
        return gigs.stream()
                .map(GigResponse::fromGig)
                .toList();
    }

    @Override
    public Optional<GigResponse> getGigById(Long id) {
        return gigRepository.findById(id)
                .map(GigResponse::fromGig);
    }

    @Override
    public ApiResponse<GigResponse> createGig(GigRequest gig) {
        if (gig.getLocationId() == null) {
            throw new BadRequestException("Location must be set");
        }

        Location location = locationRepository.findById(gig.getLocationId())
                .orElseThrow(() -> new NotFoundException("Location not found with id " + gig.getLocationId()));

        ApiResponse<User> response = userService.getCurrentLoggedInUser();
        if (response.getStatusCode() != 200 || response.getData() == null) {
            throw new BadRequestException("Current user not found or unauthorized");
        }

        User currentUser = response.getData();

        Gig newGig = Gig.builder()
                .title(gig.getTitle())
                .description(gig.getDescription())
                .type(gig.getType())
                .date(gig.getDate())
                .user(currentUser)
                .location(location)
                .createdAt(LocalDate.now().atStartOfDay())
                .updatedAt(LocalDate.now().atStartOfDay())
                .build();

        Gig savedGig = gigRepository.save(newGig);

        return new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Gig created successfully",
                GigResponse.fromGig(savedGig)
        );
    }

    @Override
    public ApiResponse<GigResponse> updateGig(Long id, GigRequest gig) {
        if (gig.getLocationId() == null) {
            throw new BadRequestException("Location must be set");
        }

        Location location = locationRepository.findById(gig.getLocationId())
                .orElseThrow(() -> new NotFoundException("Location not found with id " + gig.getLocationId()));

        ApiResponse<User> response = userService.getCurrentLoggedInUser();
        if (response.getStatusCode() != 200 || response.getData() == null) {
            throw new BadRequestException("Current user not found or unauthorized");
        }

        User currentUser = response.getData();

        Gig updatedGig = Gig.builder()
                .id(id)
                .title(gig.getTitle())
                .description(gig.getDescription())
                .type(gig.getType())
                .date(gig.getDate())
                .user(currentUser)
                .location(location)
                .createdAt(LocalDate.now().atStartOfDay())
                .updatedAt(LocalDate.now().atStartOfDay())
                .build();

        Gig savedGig = gigRepository.save(updatedGig);

        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "Gig created successfully",
                GigResponse.fromGig(savedGig)
        );
    }

    @Override
    public boolean deleteGigById(Long id) {
        if (gigRepository.existsById(id)) {
            gigRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

