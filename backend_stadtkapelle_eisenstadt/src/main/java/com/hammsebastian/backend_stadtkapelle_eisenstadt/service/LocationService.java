/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.LocationRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.LocationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LocationService {

    ApiResponse<LocationResponse> getLocation(Long id);

    ApiResponse<LocationResponse> saveLocation(LocationRequest locationRequest);

    ApiResponse<LocationResponse> updateLocation(Long id, LocationRequest locationRequest);

    ApiResponse<String> deleteLocation(Long id);

    ApiResponse<String> uploadImage(MultipartFile file);

    ApiResponse<List<LocationResponse>> getAllLocations();
}
