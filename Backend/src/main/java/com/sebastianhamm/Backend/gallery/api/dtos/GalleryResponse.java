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


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class GalleryResponse {
    private Long id;
    private String title;
    private LocalDate galleryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ImageResponse> images;

    public GalleryResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ImageResponse> images) {
        this.images = images;
    }
}
