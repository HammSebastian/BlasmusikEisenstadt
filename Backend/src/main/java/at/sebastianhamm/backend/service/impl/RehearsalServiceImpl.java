package at.sebastianhamm.backend.service.impl;

import at.sebastianhamm.backend.entity.Location;
import at.sebastianhamm.backend.entity.Rehearsal;
import at.sebastianhamm.backend.entity.User;
import at.sebastianhamm.backend.exceptions.BadRequestException;
import at.sebastianhamm.backend.exceptions.NotFoundException;
import at.sebastianhamm.backend.payload.request.RehearsalRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.RehearsalResponse;
import at.sebastianhamm.backend.repository.LocationRepository;
import at.sebastianhamm.backend.repository.RehearsalRepository;
import at.sebastianhamm.backend.service.RehearsalService;
import at.sebastianhamm.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RehearsalServiceImpl implements RehearsalService {

    private final RehearsalRepository rehearsalRepository;
    private final LocationRepository locationRepository;
    private final UserService userService;

    @Override
    public List<RehearsalResponse> getAllRehearsals() {
        List<Rehearsal> rehearsals = rehearsalRepository.findAll();
        return rehearsals.stream()
                .map(RehearsalResponse::fromRehearsal)
                .toList();
    }

    @Override
    public Optional<RehearsalResponse> getRehearsalById(Long id) {
        return rehearsalRepository.findById(id)
                .map(RehearsalResponse::fromRehearsal);
    }

    @Override
    public ApiResponse<RehearsalResponse> createRehearsal(RehearsalRequest request) {
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new NotFoundException("Location not found with id: " + request.getLocationId()));

        ApiResponse<User> response = userService.getCurrentLoggedInUser();

        if (response.getStatusCode() != 200 || response.getData() == null) {
            throw new BadRequestException("Current user not found or unauthorized");
        }
        User currentUser = response.getData();

        Rehearsal rehearsal = Rehearsal.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .date(request.getDate())
                .location(location)
                .user(currentUser)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Rehearsal saved = rehearsalRepository.save(rehearsal);
        return new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Rehearsal created successfully",
                RehearsalResponse.fromRehearsal(saved)
        );
    }

    @Override
    public ApiResponse<RehearsalResponse> updateRehearsal(Long id, RehearsalRequest request) {
        Rehearsal rehearsal = rehearsalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rehearsal not found with id: " + id));

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new NotFoundException("Location not found with id: " + request.getLocationId()));

        rehearsal.setTitle(request.getTitle());
        rehearsal.setDescription(request.getDescription());
        rehearsal.setDate(request.getDate());
        rehearsal.setLocation(location);
        rehearsal.setUpdatedAt(LocalDateTime.now());

        Rehearsal updated = rehearsalRepository.save(rehearsal);
        return ApiResponse.<RehearsalResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Rehearsal updated successfully")
                .data(RehearsalResponse.fromRehearsal(updated))
                .build();
    }

    @Override
    public boolean deleteRehearsal(Long id) {
        if (rehearsalRepository.existsById(id)) {
            rehearsalRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
