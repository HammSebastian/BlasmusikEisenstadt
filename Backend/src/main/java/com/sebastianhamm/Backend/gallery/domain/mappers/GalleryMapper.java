/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.gallery.domain.mappers;
import com.sebastianhamm.Backend.image.api.dtos.ImageResponse;
import com.sebastianhamm.Backend.image.domain.entities.ImageEntity;
import com.sebastianhamm.Backend.image.domain.mappers.ImageMapper;


import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;
import com.sebastianhamm.Backend.gallery.api.dtos.GalleryRequest;
import com.sebastianhamm.Backend.gallery.api.dtos.GalleryResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GalleryMapper {

    public static GalleryEntity toEntity(GalleryRequest request) {
        if (request == null) return null;

        GalleryEntity entity = new GalleryEntity();
        entity.setTitle(request.getTitle());
        entity.setGalleryDate(request.getGalleryDate());
        entity.setImages(request.getImages());
        return entity;
    }

    public GalleryResponse toResponse(GalleryEntity entity) {
        if (entity == null) return null;

        GalleryResponse response = new GalleryResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setGalleryDate(entity.getGalleryDate());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setImages(entity.getImages().stream()
                .map(ImageMapper::toResponse)
                .toList());

        return response;
    }

    public void updateEntity(GalleryEntity entity, GalleryRequest request) {
        if (entity == null || request == null) return;

        entity.setTitle(request.getTitle());
        entity.setGalleryDate(request.getGalleryDate());
        entity.setImages(request.getImages());
    }
}
