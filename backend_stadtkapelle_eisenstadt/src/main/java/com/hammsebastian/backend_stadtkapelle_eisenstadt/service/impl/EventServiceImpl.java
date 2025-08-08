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
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<EventResponse> getEvent(Long id) {
        if (id == null) {
            log.warn("Attempted to get event with null ID");
            return ApiResponse.<EventResponse>builder()
                    .message("Event ID cannot be null")
                    .statusCode(400)
                    .errors(new String[]{"Event ID is required"})
                    .build();
        }
        try {
            Optional<EventEntity> eventOpt = eventRepository.findById(id);
            if (eventOpt.isEmpty()) {
                log.info("Event not found with ID: {}", id);
                return ApiResponse.<EventResponse>builder()
                        .message("Event not found")
                        .statusCode(404)
                        .errors(new String[]{"No event with ID " + id})
                        .build();
            }
            EventEntity event = eventOpt.get();
            EventResponse response = EventResponse.toEventResponse(event);
            return ApiResponse.<EventResponse>builder()
                    .message("Event found")
                    .statusCode(200)
                    .data(response)
                    .errors(null)
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving event with ID: {}", id, e);
            return ApiResponse.<EventResponse>builder()
                    .message("Error retrieving event: " + e.getMessage())
                    .statusCode(500)
                    .errors(new String[]{e.getMessage()})
                    .build();
        }
    }

    @Override
    @Transactional
    public ApiResponse<EventResponse> saveEvent(EventRequest eventRequest) {
        if (eventRequest == null) {
            log.warn("Attempted to save event with null request");
            return ApiResponse.<EventResponse>builder()
                    .message("Event request cannot be null")
                    .statusCode(400)
                    .errors(new String[]{"Event request is required"})
                    .build();
        }
        try {
            Long locationId = eventRequest.getLocationId();
            if (locationId == null) {
                log.warn("Attempted to save event with null location ID");
                return ApiResponse.<EventResponse>builder()
                        .message("Location ID is required")
                        .statusCode(400)
                        .errors(new String[]{"Location ID must be provided"})
                        .build();
            }
            Optional<LocationEntity> locationOpt = locationRepository.findById(locationId);
            if (locationOpt.isEmpty()) {
                log.info("Location not found with ID: {}", locationId);
                return ApiResponse.<EventResponse>builder()
                        .message("Location not found")
                        .statusCode(404)
                        .errors(new String[]{"No location with ID " + locationId})
                        .build();
            }
            Optional<EventEntity> existing = eventRepository.findEventEntityByTitleAndLocation(eventRequest.getTitle(), locationOpt.get());
            if (existing.isPresent()) {
                log.info("Event already exists with title '{}' at location ID: {}", eventRequest.getTitle(), locationId);
                return ApiResponse.<EventResponse>builder()
                        .message("Event already exists")
                        .statusCode(409)
                        .errors(new String[]{"Event with same title and location already exists"})
                        .build();
            }
            EventEntity eventEntity = new EventEntity();
            eventEntity.setTitle(eventRequest.getTitle());
            eventEntity.setDescription(eventRequest.getDescription());
            eventEntity.setDate(eventRequest.getDate());
            eventEntity.setEventImageUrl(eventRequest.getEventImageUrl());
            eventEntity.setEventType(eventRequest.getEventType());
            eventEntity.setLocation(locationOpt.get());
            EventEntity saved = eventRepository.save(eventEntity);
            log.info("Event saved successfully with ID: {}", saved.getId());
            return ApiResponse.<EventResponse>builder()
                    .message("Event saved successfully")
                    .statusCode(201)
                    .data(EventResponse.toEventResponse(saved))
                    .errors(null)
                    .build();
        } catch (Exception e) {
            log.error("Error saving event", e);
            return ApiResponse.<EventResponse>builder()
                    .message("Error saving event: " + e.getMessage())
                    .statusCode(500)
                    .errors(new String[]{e.getMessage()})
                    .build();
        }
    }

    @Override
    @Transactional
    public ApiResponse<EventResponse> updateEvent(Long id, EventRequest eventRequest) {
        if (id == null) {
            log.warn("Attempted to update event with null ID");
            return ApiResponse.<EventResponse>builder()
                    .message("Event ID cannot be null")
                    .statusCode(400)
                    .errors(new String[]{"Event ID is required"})
                    .build();
        }
        if (eventRequest == null) {
            log.warn("Attempted to update event with null request");
            return ApiResponse.<EventResponse>builder()
                    .message("Event request cannot be null")
                    .statusCode(400)
                    .errors(new String[]{"Event request is required"})
                    .build();
        }
        try {
            Optional<EventEntity> eventOpt = eventRepository.findById(id);
            if (eventOpt.isEmpty()) {
                log.info("Event not found with ID: {}", id);
                return ApiResponse.<EventResponse>builder()
                        .message("Event not found")
                        .statusCode(404)
                        .errors(new String[]{"No event with ID " + id})
                        .build();
            }
            Long locationId = eventRequest.getLocationId();
            if (locationId == null) {
                log.warn("Attempted to update event with null location ID");
                return ApiResponse.<EventResponse>builder()
                        .message("Location ID is required")
                        .statusCode(400)
                        .errors(new String[]{"Location ID must be provided"})
                        .build();
            }
            Optional<LocationEntity> locationOpt = locationRepository.findById(locationId);
            if (locationOpt.isEmpty()) {
                log.info("Location not found with ID: {}", locationId);
                return ApiResponse.<EventResponse>builder()
                        .message("Location not found")
                        .statusCode(404)
                        .errors(new String[]{"No location with ID " + locationId})
                        .build();
            }
            EventEntity event = eventOpt.get();
            event.setTitle(eventRequest.getTitle());
            event.setDescription(eventRequest.getDescription());
            event.setDate(eventRequest.getDate());
            event.setEventImageUrl(eventRequest.getEventImageUrl());
            event.setEventType(eventRequest.getEventType());
            event.setLocation(locationOpt.get());

            EventEntity updated = eventRepository.save(event);
            log.info("Event updated successfully with ID: {}", updated.getId());
            return ApiResponse.<EventResponse>builder()
                    .message("Event updated successfully")
                    .statusCode(200)
                    .data(EventResponse.toEventResponse(updated))
                    .errors(null)
                    .build();
        } catch (Exception e) {
            log.error("Error updating event with ID: {}", id, e);
            return ApiResponse.<EventResponse>builder()
                    .message("Error updating event: " + e.getMessage())
                    .statusCode(500)
                    .errors(new String[]{e.getMessage()})
                    .build();
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteEvent(Long id) {
        if (id == null) {
            log.warn("Attempted to delete event with null ID");
            return ApiResponse.<String>builder()
                    .message("Event ID cannot be null")
                    .statusCode(400)
                    .errors(new String[]{"Event ID is required"})
                    .build();
        }
        try {
            if (!eventRepository.existsById(id)) {
                log.info("Event not found with ID: {}", id);
                return ApiResponse.<String>builder()
                        .message("Event not found")
                        .statusCode(404)
                        .errors(new String[]{"No event with ID " + id})
                        .build();
            }

            Optional<EventEntity> eventOpt = eventRepository.findById(id);
            if (eventOpt.isPresent()) {
                EventEntity event = eventOpt.get();
                String imageUrl = event.getEventImageUrl();
                eventRepository.deleteById(id);
                log.info("Event deleted successfully with ID: {}", id);
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    boolean deleted = fileStorageService.deleteFile(imageUrl);
                    if (!deleted) {
                        log.warn("Failed to delete image for event ID: {}, URL: {}", id, imageUrl);
                    }
                }
                return ApiResponse.<String>builder()
                        .message("Event deleted successfully")
                        .statusCode(200)
                        .errors(null)
                        .build();
            } else {
                // fallback - should not happen
                eventRepository.deleteById(id);
                log.info("Event deleted successfully with ID: {}", id);
                return ApiResponse.<String>builder()
                        .message("Event deleted successfully")
                        .statusCode(200)
                        .errors(null)
                        .build();
            }
        } catch (Exception e) {
            log.error("Error deleting event with ID: {}", id, e);
            return ApiResponse.<String>builder()
                    .message("Error deleting event: " + e.getMessage())
                    .statusCode(500)
                    .errors(new String[]{e.getMessage()})
                    .build();
        }
    }

    @Override
    public ApiResponse<String> uploadImage(MultipartFile file) {
        if (file == null) {
            log.warn("Attempted to upload null file");
            return ApiResponse.<String>builder()
                    .message("File cannot be null")
                    .statusCode(400)
                    .errors(new String[]{"File must not be null"})
                    .build();
        }
        if (file.isEmpty()) {
            log.warn("Attempted to upload empty file");
            return ApiResponse.<String>builder()
                    .message("Please select a file")
                    .statusCode(400)
                    .errors(new String[]{"File is empty"})
                    .build();
        }
        try {
            String imageUrl = fileStorageService.storeImage(file, Optional.of("events"), Optional.of("event-"));
            log.info("Image uploaded successfully: {}", imageUrl);
            return ApiResponse.<String>builder()
                    .message("Upload successful")
                    .statusCode(200)
                    .data(imageUrl)
                    .errors(null)
                    .build();
        } catch (IllegalArgumentException e) {
            log.warn("Invalid image upload: {}", e.getMessage());
            return ApiResponse.<String>builder()
                    .message(e.getMessage())
                    .statusCode(400)
                    .errors(new String[]{e.getMessage()})
                    .build();
        } catch (Exception e) {
            log.error("Error uploading image", e);
            return ApiResponse.<String>builder()
                    .message("Error uploading image: " + e.getMessage())
                    .statusCode(500)
                    .errors(new String[]{e.getMessage()})
                    .build();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventRepository.findAll()
                .stream()
                .map(EventResponse::toEventResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<EventResponse>>builder()
                .message("Events retrieved successfully")
                .statusCode(200)
                .data(events)
                .errors(null)
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