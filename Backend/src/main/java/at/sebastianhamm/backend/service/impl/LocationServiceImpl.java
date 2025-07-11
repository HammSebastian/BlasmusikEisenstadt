package at.sebastianhamm.backend.service.impl;

import at.sebastianhamm.backend.entity.Location;
import at.sebastianhamm.backend.entity.User;
import at.sebastianhamm.backend.exceptions.BadRequestException;
import at.sebastianhamm.backend.payload.request.LocationRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.LocationResponse;
import at.sebastianhamm.backend.repository.GigRepository;
import at.sebastianhamm.backend.repository.LocationRepository;
import at.sebastianhamm.backend.service.LocationService;
import at.sebastianhamm.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final GigRepository gigRepository;
    private final UserService userService;

    @Override
    public List<LocationResponse> getAllLocations() {
        List<Location> locations = locationRepository.findAll();

        if (locations.isEmpty()) {
            return List.of(); // leere Liste zurückgeben
        }

        // Optional: Gigs pro Location laden, falls notwendig
        for (Location location : locations) {
            location.setGigs(gigRepository.findByLocation(location));
        }

        // Mappen zu DTOs
        return locations.stream()
                .map(LocationResponse::fromLocation)
                .toList();
    }

    @Override
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    @Override
    public ApiResponse<LocationResponse> createLocation(LocationRequest location) {
        if (locationRepository.existsByCityAndCountryAndStreetAndHouseNumber(location.getCity(), location.getCountry(), location.getStreet(), location.getHouseNumber())) {
            throw new BadRequestException("Location with this address already exists");
        }

        ApiResponse<User> response = userService.getCurrentLoggedInUser();
        if (response.getStatusCode() != 200 || response.getData() == null) {
            throw new BadRequestException("Current user not found or unauthorized");
        }

        User currentUser = response.getData();

        Location newLocation = Location.builder()
                .zipCode(location.getZipCode())
                .city(location.getCity())
                .country(location.getCountry())
                .street(location.getStreet())
                .houseNumber(location.getHouseNumber())
                .user(currentUser)
                .build();

        Location savedLocation = locationRepository.save(newLocation);
        return new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Location created successfully",
                LocationResponse.fromLocation(savedLocation)
        );
    }

    @Override
    public Location updateLocation(Long id, Location location) {
        return locationRepository.findById(id)
                .map(existing -> {
                    existing.setZipCode(location.getZipCode());
                    existing.setCity(location.getCity());
                    existing.setCountry(location.getCountry());
                    existing.setStreet(location.getStreet());
                    existing.setHouseNumber(location.getHouseNumber());
                    return locationRepository.save(existing);
                })
                .orElseGet(() -> {
                    location.setId(id);
                    return locationRepository.save(location);
                });
    }

    @Override
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public List<Location> searchLocations(String query) {
        return locationRepository.findByCityContainingIgnoreCase(query);
    }
}
