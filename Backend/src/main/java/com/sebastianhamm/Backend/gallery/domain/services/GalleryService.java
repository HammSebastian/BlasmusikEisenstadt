/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.gallery.domain.services;
import com.sebastianhamm.Backend.image.api.dtos.ImageResponse;
import com.sebastianhamm.Backend.image.domain.entities.ImageEntity;

import com.sebastianhamm.Backend.gallery.api.dtos.GalleryRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.gallery.api.dtos.GalleryResponse;

import java.util.List;

public interface GalleryService {

    ApiResponse<GalleryResponse> create(GalleryRequest request);
    ApiResponse<GalleryResponse> update(Long id, GalleryRequest request);
    ApiResponse<List<GalleryResponse>> findAll();
    ApiResponse<GalleryResponse> findById(Long id);
    ApiResponse<String> delete(Long id);
}