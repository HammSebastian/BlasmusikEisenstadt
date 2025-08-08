/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.GalleryEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.ImageEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.GalleryRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.ImageRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.GalleryResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.GalleryRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.GalleryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GalleryServiceImpl implements GalleryService {

    private final GalleryRepository galleryRepository;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<GalleryResponse>> getAllGalleries() {
        try {
            List<GalleryEntity> galleryEntities = galleryRepository.findAll();
            List<GalleryResponse> galleryResponses = galleryEntities.stream()
                    .map(GalleryResponse::toGalleryResponse)
                    .toList();

            log.debug("Retrieved {} galleries", galleryEntities.size());
            return ApiResponse.<List<GalleryResponse>>builder()
                    .message("Galleries retrieved successfully")
                    .statusCode(200)
                    .data(galleryResponses)
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving all galleries", e);
            return ApiResponse.<List<GalleryResponse>>builder()
                    .message("Error retrieving galleries: " + e.getMessage())
                    .statusCode(500)
                    .build();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<GalleryResponse> getGalleryById(Long id) {
        try {
            if (id == null) {
                log.warn("Attempted to get gallery with null ID");
                return ApiResponse.<GalleryResponse>builder()
                        .message("Gallery ID cannot be null")
                        .statusCode(400)
                        .build();
            }

            Optional<GalleryEntity> galleryOptional = galleryRepository.findById(id);
            if (galleryOptional.isPresent()) {
                GalleryEntity gallery = galleryOptional.get();
                log.debug("Gallery found with ID: {}", id);
                return ApiResponse.<GalleryResponse>builder()
                        .message("Gallery retrieved successfully")
                        .statusCode(200)
                        .data(GalleryResponse.toGalleryResponse(gallery))
                        .build();
            } else {
                log.info("Gallery not found with ID: {}", id);
                return ApiResponse.<GalleryResponse>builder()
                        .message("Gallery not found")
                        .statusCode(404)
                        .build();
            }
        } catch (Exception e) {
            log.error("Error retrieving gallery with ID: {}", id, e);
            return ApiResponse.<GalleryResponse>builder()
                    .message("Error retrieving gallery: " + e.getMessage())
                    .statusCode(500)
                    .build();
        }
    }

    @Override
    @Transactional
    public ApiResponse<GalleryResponse> createGallery(GalleryRequest galleryRequest) {
        try {
            // Validate input
            if (galleryRequest == null) {
                log.warn("Attempted to create gallery with null request");
                return ApiResponse.<GalleryResponse>builder()
                        .message("Gallery request cannot be null")
                        .statusCode(400)
                        .build();
            }

            // Check if gallery already exists
            if (galleryRepository.existsByTitleAndGalleryDate(
                    galleryRequest.getTitle(), galleryRequest.getGalleryDate())) {
                log.info("Gallery already exists with title '{}' and date '{}'",
                        galleryRequest.getTitle(), galleryRequest.getGalleryDate());
                return ApiResponse.<GalleryResponse>builder()
                        .message("Gallery already exists")
                        .statusCode(409)
                        .build();
            }

            // Create gallery entity
            GalleryEntity galleryEntity = new GalleryEntity();
            galleryEntity.setTitle(galleryRequest.getTitle());
            galleryEntity.setGalleryDate(galleryRequest.getGalleryDate());
            galleryEntity.setImages(new ArrayList<>());

            // Convert ImageRequest objects to ImageEntity objects and add to gallery
            if (galleryRequest.getImages() != null && !galleryRequest.getImages().isEmpty()) {
                List<ImageEntity> imageEntities = galleryRequest.getImages().stream()
                        .map(this::convertToImageEntity)
                        .toList();
                galleryEntity.getImages().addAll(imageEntities);
            }

            // Save gallery
            GalleryEntity savedGallery = galleryRepository.save(galleryEntity);
            log.info("Gallery created successfully with ID: {}", savedGallery.getId());

            return ApiResponse.<GalleryResponse>builder()
                    .message("Gallery created successfully")
                    .statusCode(201)
                    .data(GalleryResponse.toGalleryResponse(savedGallery))
                    .build();
        } catch (Exception e) {
            log.error("Error creating gallery", e);
            return ApiResponse.<GalleryResponse>builder()
                    .message("Error creating gallery: " + e.getMessage())
                    .statusCode(500)
                    .build();
        }
    }

    @Override
    @Transactional
    public ApiResponse<GalleryResponse> updateGallery(Long id, GalleryRequest galleryRequest) {
        try {
            // Validate input
            if (id == null) {
                log.warn("Attempted to update gallery with null ID");
                return ApiResponse.<GalleryResponse>builder()
                        .message("Gallery ID cannot be null")
                        .statusCode(400)
                        .build();
            }

            if (galleryRequest == null) {
                log.warn("Attempted to update gallery with null request");
                return ApiResponse.<GalleryResponse>builder()
                        .message("Gallery request cannot be null")
                        .statusCode(400)
                        .build();
            }

            // Check if gallery exists
            Optional<GalleryEntity> galleryOptional = galleryRepository.findById(id);
            if (galleryOptional.isEmpty()) {
                log.info("Gallery not found with ID: {}", id);
                return ApiResponse.<GalleryResponse>builder()
                        .message("Gallery not found")
                        .statusCode(404)
                        .build();
            }

            // Update gallery
            GalleryEntity galleryEntity = galleryOptional.get();
            galleryEntity.setTitle(galleryRequest.getTitle());
            galleryEntity.setGalleryDate(galleryRequest.getGalleryDate());

            // Update images - clear existing and add new ones
            galleryEntity.getImages().clear();
            if (galleryRequest.getImages() != null && !galleryRequest.getImages().isEmpty()) {
                List<ImageEntity> imageEntities = galleryRequest.getImages().stream()
                        .map(this::convertToImageEntity)
                        .collect(Collectors.toList());
                galleryEntity.getImages().addAll(imageEntities);
            }

            // Save updated gallery
            GalleryEntity updatedGallery = galleryRepository.save(galleryEntity);
            log.info("Gallery updated successfully with ID: {}", updatedGallery.getId());

            return ApiResponse.<GalleryResponse>builder()
                    .message("Gallery updated successfully")
                    .statusCode(200)
                    .data(GalleryResponse.toGalleryResponse(updatedGallery))
                    .build();
        } catch (Exception e) {
            log.error("Error updating gallery with ID: {}", id, e);
            return ApiResponse.<GalleryResponse>builder()
                    .message("Error updating gallery: " + e.getMessage())
                    .statusCode(500)
                    .build();
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteGallery(Long id) {
        try {
            if (id == null) {
                log.warn("Attempted to delete gallery with null ID");
                return ApiResponse.<String>builder()
                        .message("Gallery ID cannot be null")
                        .statusCode(400)
                        .build();
            }

            if (galleryRepository.existsById(id)) {
                galleryRepository.deleteById(id);
                log.info("Gallery deleted successfully with ID: {}", id);
                return ApiResponse.<String>builder()
                        .message("Gallery deleted successfully")
                        .statusCode(200)
                        .build();
            } else {
                log.info("Gallery not found with ID: {}", id);
                return ApiResponse.<String>builder()
                        .message("Gallery not found")
                        .statusCode(404)
                        .build();
            }
        } catch (Exception e) {
            log.error("Error deleting gallery with ID: {}", id, e);
            return ApiResponse.<String>builder()
                    .message("Error deleting gallery: " + e.getMessage())
                    .statusCode(500)
                    .build();
        }
    }

    /**
     * Converts an ImageRequest to an ImageEntity.
     *
     * @param imageRequest The ImageRequest to convert
     * @return The converted ImageEntity
     */
    private ImageEntity convertToImageEntity(ImageRequest imageRequest) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageUrl(imageRequest.getImageUrl());
        imageEntity.setAuthor(imageRequest.getAuthor());
        imageEntity.setDate(imageRequest.getDate());

        return imageEntity;
    }
}
