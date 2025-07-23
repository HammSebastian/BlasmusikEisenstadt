/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.ImageRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ImageResponse;

import java.util.List;

public interface ImageService {

    ApiResponse<List<ImageResponse>> getAllImages();

    ApiResponse<ImageResponse> getImageById(Long id);

    ApiResponse<ImageResponse> saveImage(ImageRequest imageRequest);

    ApiResponse<String> deleteImage(Long id);

    ApiResponse<List<ImageResponse>> getImagesByGalleryTitle(String title);
}
