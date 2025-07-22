/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.EventRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.EventResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {

    ApiResponse<EventResponse> getEvent(Long id);

    ApiResponse<EventResponse> saveEvent(EventRequest eventRequest);

    ApiResponse<EventResponse> updateEvent(Long id, EventRequest eventRequest);

    ApiResponse<String> deleteEvent(Long id);

    ApiResponse<String> uploadImage(MultipartFile file);

    ApiResponse<List<EventResponse>> getAllEvents();
}
