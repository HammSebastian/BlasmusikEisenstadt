package at.sebastianhamm.backend.service;

import at.sebastianhamm.backend.entity.Location;
import at.sebastianhamm.backend.payload.request.LocationRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.LocationResponse;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    List<LocationResponse> getAllLocations();
    Optional<Location> getLocationById(Long id);
    ApiResponse<LocationResponse> createLocation(LocationRequest location);
    Location updateLocation(Long id, Location location);
    void deleteLocation(Long id);
    List<Location> searchLocations(String query);
}
