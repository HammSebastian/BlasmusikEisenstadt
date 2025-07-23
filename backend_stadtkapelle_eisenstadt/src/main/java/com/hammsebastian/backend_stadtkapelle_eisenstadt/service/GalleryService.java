/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.GalleryRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.GalleryResponse;

import java.util.List;

public interface GalleryService {

    ApiResponse<List<GalleryResponse>> getAllGalleries();

    ApiResponse<GalleryResponse> getGalleryById(Long id);

    ApiResponse<GalleryResponse> createGallery(GalleryRequest galleryRequest);

    ApiResponse<GalleryResponse> updateGallery(Long id, GalleryRequest galleryRequest);

    ApiResponse<String> deleteGallery(Long id);
}
