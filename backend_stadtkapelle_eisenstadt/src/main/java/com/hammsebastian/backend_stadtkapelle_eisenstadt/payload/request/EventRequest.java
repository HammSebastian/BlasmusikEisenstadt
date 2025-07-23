/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class EventRequest {

    @NotNull(message = "Title must not be null")
    private String title;

    @NotNull(message = "Description must not be null")
    private String description;

    @NotNull(message = "Date must not be null")
    private LocalDate date;

    private String eventImage;

    @NotNull(message = "Event type must not be null")
    private EventType eventType;

    @NotNull(message = "Location ID must not be null")
    private Long locationId;
}
