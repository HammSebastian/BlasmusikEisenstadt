/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.image.domain.mappers;
import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;


import com.sebastianhamm.Backend.image.domain.entities.ImageEntity;
import com.sebastianhamm.Backend.image.api.dtos.ImageResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class ImageMapper {
    
    public static ImageResponse toResponse(ImageEntity image) {
        if (image == null) return null;

        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setId(image.getId());
        imageResponse.setImageUrl(image.getImageUrl());
        imageResponse.setAuthor(image.getAuthor());
        imageResponse.setUploadDate(image.getUploadDate());
        imageResponse.setFilename(image.getFilename());
        imageResponse.setMimeType(image.getMimeType());
        imageResponse.setFileSizeBytes(image.getFileSizeBytes());

        return imageResponse;
    }
    
    public static List<ImageResponse> toResponseList(List<ImageEntity> images) {
        if (images == null) return null;
        
        return images.stream()
                .map(ImageMapper::toResponse)
                .toList();
    }
    
    public static ImageEntity toEntity(MultipartFile file, String author, String context, String mimeType, String imageUrl, String slug) {
        if (file == null) return null;
        
        ImageEntity entity = new ImageEntity();
        entity.setFilename(file.getOriginalFilename());
        entity.setMimeType(mimeType);
        entity.setFileSizeBytes(file.getSize());
        entity.setAuthor(author);
        entity.setImageUrl(imageUrl);
        entity.setSlug(slug);
        
        return entity;
    }
}
