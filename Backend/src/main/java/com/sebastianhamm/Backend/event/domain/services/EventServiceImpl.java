/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.event.domain.services;
import com.sebastianhamm.Backend.location.domain.entities.LocationEntity;


import com.sebastianhamm.Backend.event.domain.entities.EventEntity;
import com.sebastianhamm.Backend.event.domain.mappers.EventMapper;
import com.sebastianhamm.Backend.event.api.dtos.EventRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.event.api.dtos.EventResponse;
import com.sebastianhamm.Backend.event.domain.repositories.EventRepository;
import com.sebastianhamm.Backend.location.domain.repositories.LocationRepository;
import com.sebastianhamm.Backend.event.domain.services.EventService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<EventResponse> getEventById(Long id) {
        Optional<EventEntity> optional = eventRepository.findById(id);
        return optional.map(eventEntity
                -> new ApiResponse<>(200, "Event found", eventMapper.toResponse(eventEntity)))
                .orElseGet(() -> new ApiResponse<>(404, "Event not found with ID: " + id, null));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<EventResponse> findEventByName(String name) {
        Optional<EventEntity> optional = eventRepository.findEventEntityByTitle(name);
        return optional.map(eventEntity
                -> new ApiResponse<>(200, "Event found", eventMapper.toResponse(eventEntity)))
                .orElseGet(() -> new ApiResponse<>(404, "Event not found with name: " + name, null));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<EventResponse> findEventByLocation(String locationEntity) {
        Optional<LocationEntity> optionalLocation = locationRepository.findByName(locationEntity);
        if (optionalLocation.isEmpty()) {
            return new ApiResponse<>(404, "Location not found with name: " + locationEntity, null);
        }

        Optional<EventEntity> optional = eventRepository.findEventEntityByLocation(optionalLocation.get());
        return optional.map(eventEntity
                -> new ApiResponse<>(200, "Event found", eventMapper.toResponse(eventEntity)))
                .orElseGet(() -> new ApiResponse<>(404, "Event not found for the given location", null));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<EventResponse>> findEventsByLocation(String locationEntity) {
        Optional<LocationEntity> optionalLocation = locationRepository.findByName(locationEntity);
        if (optionalLocation.isEmpty()) {
            return new ApiResponse<>(404, "Location not found with name: " + locationEntity, null);
        }
        List<EventResponse> responses = eventRepository.findEventEntityByLocation(optionalLocation.get())
                .stream()
                .map(eventMapper::toResponse)
                .toList();

        if (responses.isEmpty()) {
            return new ApiResponse<>(404, "No events found for the given location", null);
        }
        return new ApiResponse<>(200, "Events found for the given location", responses);
    }

    @Override
    public ApiResponse<EventResponse> saveEvent(EventRequest eventRequest) {
        Optional<LocationEntity> optionalLocation = locationRepository.findByName(eventRequest.getLocation().getName());
        if (optionalLocation.isEmpty()) {
            return new ApiResponse<>(404, "Location not found with name: " + eventRequest.getLocation().getName(), null);
        }

        EventEntity event = eventMapper.toEntity(eventRequest, optionalLocation.get());
        EventEntity saved = eventRepository.save(event);
        return new ApiResponse<>(201, "Event created successfully", eventMapper.toResponse(saved));
    }

    @Override
    public ApiResponse<EventResponse> updateEvent(Long id, EventRequest request) {
        Optional<EventEntity> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return new ApiResponse<>(404, "Event not found with ID: " + id, null);
        }

        Optional<LocationEntity> optionalLocation = locationRepository.findByName(request.getLocation().getName());
        if (optionalLocation.isEmpty()) {
            return new ApiResponse<>(404, "Location not found with ID: " + id, null);
        }

        EventEntity event = optionalEvent.get();
        eventMapper.updateEntity(event, request, optionalLocation.get());

        EventEntity updated = eventRepository.save(event);
        return new ApiResponse<>(200, "Event updated successfully", eventMapper.toResponse(updated));
    }

    @Override
    public ApiResponse<String> deleteEvent(Long id) {
        Optional<EventEntity> optional = eventRepository.findById(id);
        if (optional.isEmpty()) {
            return new ApiResponse<>(404, "Event not found with ID: " + id, null);
        }
        eventRepository.deleteById(id);
        return new ApiResponse<>(204, "Event deleted successfully", null);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<EventResponse>> findAllEvents() {
        List<EventResponse> responses = eventRepository.findAll().stream()
                .map(eventMapper::toResponse)
                .toList();

        return new ApiResponse<>(200, "All events found", responses);
    }

    public EventServiceImpl(EventRepository eventRepository, LocationRepository locationRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.eventMapper = eventMapper;
    }
}