/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/29/25
 */
package com.sebastianhamm.Backend.location.domain.services;
import com.sebastianhamm.Backend.event.domain.entities.EventEntity;


import com.sebastianhamm.Backend.location.domain.entities.LocationEntity;
import com.sebastianhamm.Backend.location.api.dtos.LocationRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.location.api.dtos.LocationResponse;
import com.sebastianhamm.Backend.location.domain.repositories.LocationRepository;
import com.sebastianhamm.Backend.location.domain.services.LocationService;
import com.sebastianhamm.Backend.location.domain.mappers.LocationMapper;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public ApiResponse<LocationResponse> getLocation(int id) {
        Optional<LocationEntity> location = locationRepository.findById(id);

        if (location.isEmpty()) {
            return new ApiResponse<>(404, "Location not found", null);
        }

        LocationEntity locationEntity = location.get();
        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setName(locationEntity.getName());
        locationResponse.setAddress(locationEntity.getAddress());

        return new ApiResponse<>(200, "Location found", locationResponse);
    }

    @Override
    public ApiResponse<LocationResponse> saveLocation(LocationRequest locationRequest) {
        Optional<LocationEntity> location = locationRepository.getLocationEntityByAddress(locationRequest.getAddress());

        if (location.isEmpty()) {
            LocationEntity locationEntity = new LocationEntity();
            locationEntity.setName(locationRequest.getName());
            locationEntity.setAddress(locationRequest.getAddress());

            locationRepository.save(locationEntity);
            return new ApiResponse<>(201, "Location saved", null);
        } else {
            return new ApiResponse<>(409, "Location already exists", null);
        }
    }

    @Override
    public ApiResponse<LocationResponse> updateLocation(int id, LocationRequest locationRequest) {
        Optional<LocationEntity> location = locationRepository.findById(id);

        if (location.isEmpty()) {
            return new ApiResponse<>(404, "Location not found", null);
        }

        LocationEntity locationEntity = location.get();
        locationEntity.setName(locationRequest.getName());
        locationEntity.setAddress(locationRequest.getAddress());
        locationRepository.save(locationEntity);
        return new ApiResponse<>(200, "Location updated", null);
    }

    @Override
    public ApiResponse<String> deleteLocation(int id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
            return new ApiResponse<>(200, "Location deleted", null);
        } else {
            return new ApiResponse<>(404, "Location not found", null);
        }
    }

    @Override
    public ApiResponse<List<LocationResponse>> getAllLocations() {
        List<LocationEntity> locationEntities = locationRepository.findAll();
        List<LocationResponse> locationResponses = locationEntities.stream()
                .map(locationEntity -> {
                    LocationResponse response = new LocationResponse();
                    response.setName(locationEntity.getName());
                    response.setAddress(locationEntity.getAddress());
                    return response;
                })
                .toList();
        return new ApiResponse<>(200, "Locations retrieved", locationResponses);
    }

    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }
}
