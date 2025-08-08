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


import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;
import com.sebastianhamm.Backend.gallery.domain.mappers.GalleryMapper;
import com.sebastianhamm.Backend.image.domain.mappers.ImageMapper;
import com.sebastianhamm.Backend.gallery.api.dtos.GalleryRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.gallery.api.dtos.GalleryResponse;
import com.sebastianhamm.Backend.gallery.domain.repositories.GalleryRepository;
import com.sebastianhamm.Backend.gallery.domain.services.GalleryService;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
public class GalleryServiceImpl implements GalleryService {

    private static final Logger logger = LoggerFactory.getLogger(GalleryServiceImpl.class);
    
    private final GalleryRepository galleryRepository;
    private final GalleryMapper galleryMapper;

    @Override
    public ApiResponse<GalleryResponse> create(GalleryRequest request) {
        try {
            GalleryEntity entity = GalleryMapper.toEntity(request);
            GalleryEntity saved = galleryRepository.save(entity);
            return new ApiResponse<>(201, "Gallery created successfully", galleryMapper.toResponse(saved));
        } catch (Exception e) {
            logger.error("Failed to create gallery: {}", e.getMessage(), e);
            return new ApiResponse<>(500, "Internal server error", null);
        }
    }

    @Override
    public ApiResponse<GalleryResponse> update(Long id, GalleryRequest request) {
        return galleryRepository.findById(id)
                .map(existing -> {
                    galleryMapper.updateEntity(existing, request);
                    existing.setUpdatedAt(LocalDateTime.now());
                    GalleryEntity updated = galleryRepository.save(existing);
                    return new ApiResponse<>(200, "Gallery updated successfully", galleryMapper.toResponse(updated));
                })
                .orElseGet(() -> new ApiResponse<>(404, "Gallery not found with id: " + id, null));
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public ApiResponse<GalleryResponse> findById(Long id) {
        return galleryRepository.findById(id)
                .map(entity -> new ApiResponse<>(200, "Gallery found", galleryMapper.toResponse(entity)))
                .orElseGet(() -> new ApiResponse<>(404, "Gallery not found with id: " + id, null));
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public ApiResponse<List<GalleryResponse>> findAll() {
        try {
            List<GalleryResponse> galleries = galleryRepository.findAll()
                    .stream()
                    .map(galleryMapper::toResponse)
                    .toList();
            return new ApiResponse<>(200, "Galleries retrieved successfully", galleries);
        } catch (Exception e) {
            logger.error("Failed to retrieve all galleries: {}", e.getMessage(), e);
            return new ApiResponse<>(500, "Internal server error", null);
        }
    }

    @Override
    public ApiResponse<String> delete(Long id) {
        return galleryRepository.findById(id)
                .map(entity -> {
                    entity.setDeletedAt(LocalDateTime.now());
                    galleryRepository.save(entity);
                    return new ApiResponse<>(204, "Gallery deleted successfully", "");
                })
                .orElseGet(() -> new ApiResponse<>(404, "Gallery not found with id: " + id, null));
    }


    public GalleryServiceImpl(GalleryRepository galleryRepository, GalleryMapper galleryMapper) {
        this.galleryRepository = galleryRepository;
        this.galleryMapper = galleryMapper;
    }
}