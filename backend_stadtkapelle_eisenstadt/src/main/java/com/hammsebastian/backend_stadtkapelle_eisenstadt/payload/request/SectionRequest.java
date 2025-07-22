/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionRequest {

    @NotNull(message = "Year must not be null")
    private int year;

    @NotNull(message = "Description must not be null")
    @Size(max = 10000, message = "Description must be less than 10000 characters long")
    private String description;
}
