/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.event.domain.services;
import com.sebastianhamm.Backend.location.domain.entities.LocationEntity;

import com.sebastianhamm.Backend.event.api.dtos.EventRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.event.api.dtos.EventResponse;

import java.util.List;

public interface EventService {

    ApiResponse<EventResponse> getEventById(Long id);
    ApiResponse<EventResponse> findEventByName(String name);
    ApiResponse<EventResponse> findEventByLocation(String locationEntity);
    ApiResponse<List<EventResponse>> findEventsByLocation(String locationEntity);
    ApiResponse<EventResponse> saveEvent(EventRequest eventRequest);
    ApiResponse<EventResponse> updateEvent(Long id, EventRequest eventRequest);
    ApiResponse<String> deleteEvent(Long id);
    ApiResponse<List<EventResponse>> findAllEvents();
}
