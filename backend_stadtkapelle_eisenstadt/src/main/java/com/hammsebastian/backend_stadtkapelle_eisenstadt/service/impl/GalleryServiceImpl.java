/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.GalleryEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.GalleryRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.GalleryResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.GalleryRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {

    private final GalleryRepository galleryRepository;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<GalleryResponse>> getAllGalleries() {
        List<GalleryEntity> galleryEntities = galleryRepository.findAll();
        List<GalleryResponse> galleryResponses = galleryEntities.stream()
                .map(GalleryResponse::toGalleryResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<GalleryResponse>>builder()
                .message("Galleries retrieved successfully")
                .statusCode(200)
                .data(galleryResponses)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<GalleryResponse> getGalleryById(Long id) {
        if (galleryRepository.findById(id).isPresent()) {
            return ApiResponse.<GalleryResponse>builder()
                    .message("Gallery retrieved successfully")
                    .statusCode(200)
                    .data(GalleryResponse.toGalleryResponse(galleryRepository.findById(id).get()))
                    .build();
        } else {
            return ApiResponse.<GalleryResponse>builder()
                    .message("Gallery not found")
                    .statusCode(404)
                    .build();
        }
    }

    @Override
    @Transactional
    public ApiResponse<GalleryResponse> createGallery(GalleryRequest galleryRequest) {
        if (galleryRepository.findByTitleAndFromDate(galleryRequest.getTitle(), galleryRequest.getFromDate())) {
            return ApiResponse.<GalleryResponse>builder()
                    .message("Gallery already exists")
                    .statusCode(409)
                    .build();
        } else {
            GalleryEntity galleryEntity = GalleryEntity.builder()
                    .title(galleryRequest.getTitle())
                    .fromDate(galleryRequest.getFromDate())
                    .images(galleryRequest.getImages())
                    .build();
            galleryRepository.save(galleryEntity);
            return ApiResponse.<GalleryResponse>builder()
                    .message("Gallery created successfully")
                    .statusCode(201)
                    .data(GalleryResponse.toGalleryResponse(galleryEntity))
                    .build();
        }
    }

    @Override
    @Transactional
    public ApiResponse<GalleryResponse> updateGallery(Long id, GalleryRequest galleryRequest) {
        if (galleryRepository.findById(id).isPresent()) {
            GalleryEntity galleryEntity = galleryRepository.findById(id).get();
            galleryEntity.setTitle(galleryRequest.getTitle());
            galleryEntity.setFromDate(galleryRequest.getFromDate());
            galleryEntity.setImages(galleryRequest.getImages());
            galleryRepository.save(galleryEntity);
            return ApiResponse.<GalleryResponse>builder()
                    .message("Gallery updated successfully")
                    .statusCode(200)
                    .data(GalleryResponse.toGalleryResponse(galleryEntity))
                    .build();
        } else {
            return ApiResponse.<GalleryResponse>builder()
                    .message("Gallery not found")
                    .statusCode(404)
                    .build();
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteGallery(Long id) {
        if (galleryRepository.findById(id).isPresent()) {
            galleryRepository.deleteById(id);
            return ApiResponse.<String>builder()
                    .message("Gallery deleted")
                    .statusCode(200)
                    .build();
        } else {
            return ApiResponse.<String>builder()
                    .message("Gallery not found")
                    .statusCode(404)
                    .build();
        }
    }
}
