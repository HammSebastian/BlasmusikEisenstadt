/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.ImageEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ImageResponse {

    private String imageUrl;
    private String author;
    private LocalDate date;

    public static ImageResponse toImageResponse(ImageEntity imageEntity) {
        return ImageResponse.builder()
                .imageUrl(imageEntity.getImageUrl())
                .author(imageEntity.getAuthor())
                .date(imageEntity.getDate())
                .build();
    }
}
