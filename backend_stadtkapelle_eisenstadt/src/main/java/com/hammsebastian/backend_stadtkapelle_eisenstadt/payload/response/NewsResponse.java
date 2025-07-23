/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/21/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.NewEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class NewsResponse {

    private String title;
    private String description;
    private String newsImage;
    private String newsType;
    private LocalDate date;
    private boolean isPublished;

    public static NewsResponse toNewsResponse(NewEntity newEntity) {
        return NewsResponse.builder()
                .title(newEntity.getTitle())
                .description(newEntity.getDescription())
                .newsImage(newEntity.getNewsImage())
                .newsType(newEntity.getNewsType().toString())
                .date(newEntity.getDate())
                .isPublished(newEntity.isPublished())
                .build();
    }
}
