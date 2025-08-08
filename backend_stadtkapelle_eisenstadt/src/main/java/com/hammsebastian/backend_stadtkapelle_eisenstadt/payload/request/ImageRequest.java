/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ImageRequest {

    @NotNull(message = "Image must not be null")
    private String imageUrl;

    @NotNull(message = "Author must not be null")
    private String author;

    @NotNull(message = "Date must not be null")
    private LocalDate date;
}
