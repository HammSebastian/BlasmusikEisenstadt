/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.LocationEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.LocationRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.LocationResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.LocationRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public ApiResponse<LocationResponse> getLocation(Long id) {
        Optional<LocationEntity> location = locationRepository.findById(id);

        if (location.isEmpty()) {
            return ApiResponse.<LocationResponse>builder()
                    .message("Location not found")
                    .statusCode(404)
                    .build();
        }

        LocationEntity locationEntity = location.get();
        LocationResponse locationResponse = LocationResponse.builder()
                .id(locationEntity.getId())
                .country(locationEntity.getCountry())
                .zipCode(locationEntity.getZipCode())
                .city(locationEntity.getCity())
                .street(locationEntity.getStreet())
                .number(locationEntity.getNumber())
                .build();
        return ApiResponse.<LocationResponse>builder()
                .message("Location found")
                .statusCode(200)
                .data(locationResponse)
                .build();
    }

    @Override
    public ApiResponse<LocationResponse> saveLocation(LocationRequest locationRequest) {
        Optional<LocationEntity> location = locationRepository.getLocationEntityByCityAndZipCodeAndCountry(locationRequest.getCity(), locationRequest.getZipCode(), locationRequest.getCountry());

        if (location.isEmpty()) {
            LocationEntity locationEntity = LocationEntity.builder()
                    .country(locationRequest.getCountry())
                    .zipCode(locationRequest.getZipCode())
                    .city(locationRequest.getCity())
                    .street(locationRequest.getStreet())
                    .number(locationRequest.getNumber())
                    .build();
            locationRepository.save(locationEntity);
            return ApiResponse.<LocationResponse>builder()
                    .message("Location saved")
                    .statusCode(201)
                    .build();
        } else {
            return ApiResponse.<LocationResponse>builder()
                    .message("Location already exists")
                    .statusCode(409)
                    .build();
        }
    }

    @Override
    public ApiResponse<LocationResponse> updateLocation(Long id, LocationRequest locationRequest) {
        Optional<LocationEntity> location = locationRepository.findById(id);

        if (location.isEmpty()) {
            return ApiResponse.<LocationResponse>builder()
                    .message("Location not found")
                    .statusCode(404)
                    .build();
        }

        LocationEntity locationEntity = location.get();
        locationEntity.setCountry(locationRequest.getCountry());
        locationEntity.setZipCode(locationRequest.getZipCode());
        locationEntity.setCity(locationRequest.getCity());
        locationEntity.setStreet(locationRequest.getStreet());
        locationEntity.setNumber(locationRequest.getNumber());
        locationRepository.save(locationEntity);
        return ApiResponse.<LocationResponse>builder()
                .message("Location updated")
                .statusCode(200)
                .build();
    }

    @Override
    public ApiResponse<String> deleteLocation(Long id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
            return ApiResponse.<String>builder()
                    .message("Location deleted")
                    .statusCode(200)
                    .build();
        } else {
            return ApiResponse.<String>builder()
                    .message("Location not found")
                    .statusCode(404)
                    .build();
        }
    }

    @Override
    public ApiResponse<String> uploadImage(MultipartFile file) {
        final Path uploadDirectory = Paths.get("src/main/resources/static/images");

        if (!Files.exists(uploadDirectory)) {
            try {
                Files.createDirectories(uploadDirectory);
            } catch (IOException e) {
                return ApiResponse.<String>builder()
                        .message("Could not create upload directory")
                        .statusCode(500)
                        .data(null)
                        .build();
            }
        }

        if (file.isEmpty()) {
            return ApiResponse.<String>builder()
                    .message("Please select a file")
                    .statusCode(400)
                    .data(null)
                    .build();
        }

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String ext = "";

        int extIndex = filename.lastIndexOf('.');
        if (extIndex > 0) {
            ext = filename.substring(extIndex);
        }
        String newFilename = "site-bg-" + System.currentTimeMillis() + ext;

        try {
            Path targetPath = uploadDirectory.resolve(newFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String publicPath = "http://localhost:8081/api/v1/images/" + newFilename; // TODO: Production Pfad anpassen

            return ApiResponse.<String>builder()
                    .message("Upload erfolgreich")
                    .statusCode(200)
                    .data(publicPath)
                    .build();
        } catch (IOException e) {
            return ApiResponse.<String>builder()
                    .message("Fehler beim Upload")
                    .statusCode(500)
                    .build();
        }
    }

    @Override
    public ApiResponse<List<LocationResponse>> getAllLocations() {
        List<LocationEntity> locationEntities = locationRepository.findAll();
        List<LocationResponse> locationResponses = locationEntities.stream()
                .map(LocationResponse::toLocationResponse)
                .collect(Collectors.toList());
        
        return ApiResponse.<List<LocationResponse>>builder()
                .message("Locations retrieved successfully")
                .statusCode(200)
                .data(locationResponses)
                .build();
    }
}
