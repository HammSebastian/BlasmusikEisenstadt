/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Represents the request payload for creating a new Gallery.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class GalleryRequest {

    /**
     * The title of the gallery.
     */
    @NotBlank(message = "Title must not be blank.")
    @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters long.")
    private String title;

    /**
     * The date associated with the gallery.
     */
    @NotNull(message = "Gallery date must not be null.")
    private LocalDate galleryDate;

    /**
     * A list of images to be included in this gallery. Each image will be validated.
     */
    @NotNull(message = "Images list must not be null.")
    @Size(min = 1, message = "At least one image is required.")
    private List<@Valid ImageRequest> images;
}