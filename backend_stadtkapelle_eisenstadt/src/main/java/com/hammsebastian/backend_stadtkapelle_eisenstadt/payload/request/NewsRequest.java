/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/21/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.NewsType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class NewsRequest {

    @NotNull(message = "Title must not be null")
    private String title;

    @NotNull(message = "Description must not be null")
    private String description;

    @NotNull(message = "News image must not be null")
    @Column(name = "news_image")
    private String newsImage;

    @NotNull(message = "News type must not be null")
    @Column(name = "news_type")
    @Enumerated(EnumType.STRING)
    private NewsType newsType;

    @NotNull(message = "Date must not be null")
    private LocalDate date;

    @NotNull(message = "Is published must not be null")
    @Column(name = "is_published")
    private boolean isPublished;
}
