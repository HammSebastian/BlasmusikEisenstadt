/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.gallery.api.dtos;
import com.sebastianhamm.Backend.image.api.dtos.ImageResponse;
import com.sebastianhamm.Backend.image.domain.entities.ImageEntity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class GalleryRequest {

    @NotNull(message = "Title cannot be null")
    private String title;

    @NotBlank(message = "Gallery date cannot be blank")
    private LocalDate galleryDate;

    @NotNull(message = "Images cannot be null")
    private List<ImageEntity> images;

    public GalleryRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getGalleryDate() {
        return galleryDate;
    }

    public void setGalleryDate(LocalDate galleryDate) {
        this.galleryDate = galleryDate;
    }

    public List<ImageEntity> getImages() {
        return images;
    }

    public void setImages(List<ImageEntity> images) {
        this.images = images;
    }
}
