/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.GalleryEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.ImageEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class GalleryResponse {

    private String title;
    private LocalDate fromDate;
    private List<String> images;

    public static GalleryResponse toGalleryResponse(GalleryEntity galleryEntity) {
        List<String> imageUrls = galleryEntity.getImages().stream()
                .map(ImageEntity::getImageUrl)
                .collect(Collectors.toList());
                
        return GalleryResponse.builder()
                .title(galleryEntity.getTitle())
                .fromDate(galleryEntity.getFromDate())
                .images(imageUrls)
                .build();
    }
}
