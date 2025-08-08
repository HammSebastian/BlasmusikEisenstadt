/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.image.domain.services;
import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;

import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.image.api.dtos.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    ApiResponse<ImageResponse> saveImage(MultipartFile file, String context);
    ApiResponse<ImageResponse> updateImage(Long id, MultipartFile file, String context);
    ApiResponse<ImageResponse> findById(Long id);
    ApiResponse<List<ImageResponse>> findAll();
    ApiResponse<String> delete(Long id);
    ApiResponse<List<ImageResponse>> findBySlug(String title);
}
