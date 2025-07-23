/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.ImageEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.ImageRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ImageResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.GalleryRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.ImageRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final GalleryRepository galleryRepository;

    @Override
    public ApiResponse<List<ImageResponse>> getAllImages() {
        List<ImageEntity> imageEntities = imageRepository.findAll();
        List<ImageResponse> imageResponses = imageEntities.stream()
                .map(ImageResponse::toImageResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<ImageResponse>>builder()
                .message("Images retrieved successfully")
                .statusCode(200)
                .data(imageResponses)
                .build();
    }

    @Override
    public ApiResponse<ImageResponse> getImageById(Long id) {
        if (imageRepository.findById(id).isPresent()) {
            return ApiResponse.<ImageResponse>builder()
                    .message("Image retrieved successfully")
                    .statusCode(200)
                    .data(ImageResponse.toImageResponse(imageRepository.findById(id).get()))
                    .build();
        } else {
            return ApiResponse.<ImageResponse>builder()
                    .message("Image not found")
                    .statusCode(404)
                    .build();
        }
    }

    @Override
    public ApiResponse<ImageResponse> saveImage(ImageRequest imageRequest) {
        if (imageRepository.findImageEntitiesByAuthorAndImageUrl(imageRequest.getImageUrl(), imageRequest.getAuthor())) {
            return ApiResponse.<ImageResponse>builder()
                    .message("Image already exists")
                    .statusCode(409)
                    .build();
        } else {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setImageUrl(imageRequest.getImageUrl());
            imageEntity.setAuthor(imageRequest.getAuthor());
            imageEntity.setDate(imageRequest.getDate());
            imageRepository.save(imageEntity);
            return ApiResponse.<ImageResponse>builder()
                    .message("Image saved successfully")
                    .statusCode(201)
                    .data(ImageResponse.toImageResponse(imageEntity))
                    .build();
        }
    }

    @Override
    public ApiResponse<String> deleteImage(Long id) {
        if (imageRepository.findById(id).isPresent()) {
            imageRepository.deleteById(id);
            return ApiResponse.<String>builder()
                    .message("Image deleted")
                    .statusCode(200)
                    .build();
        } else {
            return ApiResponse.<String>builder()
                    .message("Image not found")
                    .statusCode(404)
                    .build();
        }
    }

    @Override
    public ApiResponse<List<ImageResponse>> getImagesByGalleryTitle(String title) {
        if (galleryRepository.findGalleryEntityByTitle(title).isPresent()) {
            return ApiResponse.<List<ImageResponse>>builder()
                    .message("Images retrieved successfully")
                    .statusCode(200)
                    .data(imageRepository.findByGallery_Id(galleryRepository.findGalleryEntityByTitle(title).get().getId()).stream()
                            .map(ImageResponse::toImageResponse)
                            .collect(Collectors.toList()))
                    .build();
        } else {
            return ApiResponse.<List<ImageResponse>>builder()
                    .message("Gallery not found")
                    .statusCode(404)
                    .build();
        }
    }
}
