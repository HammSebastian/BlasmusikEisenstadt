/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.EventEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.LocationEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.EventRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.EventResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.LocationResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.EventRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.LocationRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.EventService;
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
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    @Override
    public ApiResponse<EventResponse> getEvent(Long id) {
        Optional<EventEntity> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            return ApiResponse.<EventResponse>builder()
                    .message("Event not found")
                    .statusCode(404)
                    .build();
        }

        EventEntity eventEntity = event.get();
        EventResponse eventResponse = EventResponse.builder()
                .title(eventEntity.getTitle())
                .description(eventEntity.getDescription())
                .date(eventEntity.getDate())
                .eventImage(eventEntity.getEventImage())
                .eventType(eventEntity.getEventType().toString())
                .location(mapToLocationResponse(eventEntity.getLocation()))
                .build();
        return ApiResponse.<EventResponse>builder()
                .message("Event found")
                .statusCode(200)
                .data(eventResponse)
                .build();
    }

    @Override
    public ApiResponse<EventResponse> saveEvent(EventRequest eventRequest) {
        // Get location by ID instead of using the location entity directly
        Long locationId = eventRequest.getLocationId();
        if (locationId == null) {
            return ApiResponse.<EventResponse>builder()
                    .message("Location ID is required")
                    .statusCode(400)
                    .build();
        }
        
        Optional<LocationEntity> location = locationRepository.findById(locationId);

        if (location.isEmpty()) {
            return ApiResponse.<EventResponse>builder()
                    .message("Location not found")
                    .statusCode(404)
                    .build();
        }

        Optional<EventEntity> event = eventRepository.findEventEntityByTitleAndLocation(eventRequest.getTitle(), location.get());

        if (event.isEmpty()) {
            EventEntity eventEntity = EventEntity.builder()
                    .title(eventRequest.getTitle())
                    .description(eventRequest.getDescription())
                    .date(eventRequest.getDate())
                    .eventImage(eventRequest.getEventImage())
                    .eventType(eventRequest.getEventType())
                    .location(location.get())
                    .build();
            EventEntity savedEvent = eventRepository.save(eventEntity);
            
            // Return the saved event in the response
            EventResponse eventResponse = EventResponse.toEventResponse(savedEvent);
            return ApiResponse.<EventResponse>builder()
                    .message("Event saved")
                    .statusCode(201)
                    .data(eventResponse)
                    .build();
        } else {
            return ApiResponse.<EventResponse>builder()
                    .message("Event already exists")
                    .statusCode(409)
                    .build();
        }
    }

    @Override
    public ApiResponse<EventResponse> updateEvent(Long id, EventRequest eventRequest) {
        Optional<EventEntity> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            return ApiResponse.<EventResponse>builder()
                    .message("Event not found")
                    .statusCode(404)
                    .build();
        }

        // Get location by ID
        Optional<LocationEntity> location = locationRepository.findById(eventRequest.getLocationId());

        if (location.isEmpty()) {
            return ApiResponse.<EventResponse>builder()
                    .message("Location not found")
                    .statusCode(404)
                    .build();
        }

        EventEntity eventEntity = event.get();

        eventEntity.setTitle(eventRequest.getTitle());
        eventEntity.setDescription(eventRequest.getDescription());
        eventEntity.setDate(eventRequest.getDate());
        eventEntity.setEventImage(eventRequest.getEventImage());
        eventEntity.setEventType(eventRequest.getEventType());
        eventEntity.setLocation(location.get());

        EventEntity updatedEvent = eventRepository.save(eventEntity);
        
        // Return the updated event in the response
        EventResponse eventResponse = EventResponse.toEventResponse(updatedEvent);
        return ApiResponse.<EventResponse>builder()
                .message("Event updated")
                .statusCode(200)
                .data(eventResponse)
                .build();
    }

    @Override
    public ApiResponse<String> deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return ApiResponse.<String>builder()
                    .message("Event deleted")
                    .statusCode(200)
                    .build();
        } else {
            return ApiResponse.<String>builder()
                    .message("Event not found")
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
    public ApiResponse<List<EventResponse>> getAllEvents() {
        List<EventEntity> eventEntities = eventRepository.findAll();
        List<EventResponse> eventResponses = eventEntities.stream()
                .map(EventResponse::toEventResponse)
                .collect(Collectors.toList());
        
        return ApiResponse.<List<EventResponse>>builder()
                .message("Events retrieved successfully")
                .statusCode(200)
                .data(eventResponses)
                .build();
    }

    private LocationResponse mapToLocationResponse(LocationEntity location) {
        return LocationResponse.builder()
                .id(location.getId())
                .country(location.getCountry())
                .zipCode(location.getZipCode())
                .city(location.getCity())
                .street(location.getStreet())
                .number(location.getNumber())
                .build();
    }
}
