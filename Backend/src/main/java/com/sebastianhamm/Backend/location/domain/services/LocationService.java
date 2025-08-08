/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/29/25
 */
package com.sebastianhamm.Backend.location.domain.services;
import com.sebastianhamm.Backend.event.domain.entities.EventEntity;

import com.sebastianhamm.Backend.location.api.dtos.LocationRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.location.api.dtos.LocationResponse;

import java.util.List;

public interface LocationService {

    ApiResponse<LocationResponse> getLocation(int id);

    ApiResponse<LocationResponse> saveLocation(LocationRequest locationRequest);

    ApiResponse<LocationResponse> updateLocation(int id, LocationRequest locationRequest);

    ApiResponse<String> deleteLocation(int id);

    ApiResponse<List<LocationResponse>> getAllLocations();
}
