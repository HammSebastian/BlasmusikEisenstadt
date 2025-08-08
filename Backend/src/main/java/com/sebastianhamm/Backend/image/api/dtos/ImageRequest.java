/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.image.api.dtos;
import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;


import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class ImageRequest {

    @NotNull(message = "File must not be null")
    private MultipartFile file;

    @NotNull(message = "Context must not be null")
    private String context;

    public ImageRequest() {
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
