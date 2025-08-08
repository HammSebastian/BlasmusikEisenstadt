/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.event.domain.mappers;
import com.sebastianhamm.Backend.location.domain.entities.LocationEntity;


import com.sebastianhamm.Backend.event.domain.entities.EventEntity;
import com.sebastianhamm.Backend.event.api.dtos.EventRequest;
import com.sebastianhamm.Backend.event.api.dtos.EventResponse;

import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventResponse toResponse(EventEntity entity) {
        if (entity == null) return null;

        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(entity.getId());
        eventResponse.setTitle(entity.getTitle());
        eventResponse.setDescription(entity.getDescription());
        eventResponse.setEventImageUrl(entity.getEventImageUrl());
        eventResponse.setEventType(entity.getEventType());
        eventResponse.setLocation(entity.getLocation().toString());
        eventResponse.setCreatedAt(entity.getCreatedAt());
        eventResponse.setUpdatedAt(entity.getUpdatedAt());

        return eventResponse;
    }

    public EventEntity toEntity(EventRequest request, LocationEntity location) {
        if (request == null) return null;

        EventEntity entity = new EventEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setDate(request.getDate());
        entity.setEventImageUrl(request.getEventImageUrl());
        entity.setEventType(request.getEventType());
        entity.setLocation(location);
        return entity;
    }

    public void updateEntity(EventEntity entity, EventRequest request, LocationEntity location) {
        if (entity == null || request == null) return;

        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setDate(request.getDate());
        entity.setEventImageUrl(request.getEventImageUrl());
        entity.setEventType(request.getEventType());
        entity.setLocation(location);
    }
}


