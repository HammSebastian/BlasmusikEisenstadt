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
public class HistoryRequest {

    @NotNull(message = "Name must not be null")
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    @NotNull(message = "Section ID must not be null")
    private Long sectionId;

}
