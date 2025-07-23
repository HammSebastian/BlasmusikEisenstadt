/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.ImageEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class GalleryRequest {

    @NotNull(message = "Title must not be null")
    @Size(min = 2, message = "Title must be at least 2 characters long")
    private String title;

    @NotNull(message = "From date must not be null")
    private LocalDate fromDate;

    @NotNull(message = "Images must not be null")
    private List<ImageEntity> images;
}
