package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.entity.Location;
import at.sebastianhamm.backend.exceptions.NotFoundException;
import at.sebastianhamm.backend.payload.request.LocationRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.LocationResponse;
import at.sebastianhamm.backend.repository.UserRepository;
import at.sebastianhamm.backend.service.LocationService;
import at.sebastianhamm.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    private final LocationService locationService;
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LocationResponse>>> getAllLocations() {
        logger.info("Fetching all locations");
        List<LocationResponse> locations = locationService.getAllLocations();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Locations fetched successfully", locations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Location>> getLocationById(@PathVariable Long id) {
        logger.info("Fetching location with id {}", id);
        Location location = locationService.getLocationById(id)
                .orElseThrow(() -> new NotFoundException("Location not found with id: " + id));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Location fetched successfully", location));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('REPORTER')")
    public ResponseEntity<ApiResponse<LocationResponse>> createLocation(@Valid @RequestBody LocationRequest locationRequest) {
        logger.info("Creating new location");
        ApiResponse<LocationResponse> created = locationService.createLocation(locationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('REPORTER')")
    public ResponseEntity<ApiResponse<Location>> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequest locationRequest) {
        logger.info("Updating location with id {}", id);
        Location location = new Location();
        mapLocationRequestToEntity(locationRequest, location);
        Location updated = locationService.updateLocation(id, location);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Location updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('REPORTER')")
    public ResponseEntity<ApiResponse<Void>> deleteLocation(@PathVariable Long id) {
        logger.info("Deleting location with id {}", id);
        locationService.deleteLocation(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Location deleted successfully", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Location>>> searchLocations(@RequestParam String query) {
        logger.info("Searching locations with query: {}", query);
        List<Location> locations = locationService.searchLocations(query);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Locations found", locations));
    }

    private void mapLocationRequestToEntity(LocationRequest request, Location location) {
        location.setZipCode(request.getZipCode());
        location.setCity(request.getCity());
        location.setCountry(request.getCountry());
        location.setStreet(request.getStreet());
        location.setHouseNumber(request.getHouseNumber());
    }
}
