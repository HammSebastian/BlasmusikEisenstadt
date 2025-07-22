/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/21/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.NewsEntity;
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

    public static NewsResponse toNewsResponse(NewsEntity newsEntity) {
        return NewsResponse.builder()
                .title(newsEntity.getTitle())
                .description(newsEntity.getDescription())
                .newsImage(newsEntity.getNewsImage())
                .newsType(newsEntity.getNewsType().toString())
                .date(newsEntity.getDate())
                .isPublished(newsEntity.isPublished())
                .build();
    }
}
