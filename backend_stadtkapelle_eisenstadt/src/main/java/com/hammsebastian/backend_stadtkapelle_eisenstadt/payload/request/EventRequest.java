/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Represents the request payload for creating or updating an Event.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.EventType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class EventRequest {

    /**
     * The title of the event.
     */
    @NotBlank(message = "Title must not be blank.")
    @Size(max = 255, message = "Title cannot exceed 255 characters.")
    private String title;

    /**
     * A detailed description of the event.
     */
    @NotBlank(message = "Description must not be blank.")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters.")
    private String description;

    /**
     * The date of the event. Must be today or in the future.
     */
    @NotNull(message = "Date must not be null.")
    @FutureOrPresent(message = "Event date must be in the present or future.")
    private LocalDate date;

    /**
     * The URL to an image for the event. Optional.
     */
    @URL(message = "Event image must be a valid URL.")
    @Size(max = 500, message = "Image URL must not exceed 500 characters.")
    private String eventImageUrl;

    /**
     * The type of the event.
     */
    @NotNull(message = "Event type must not be null.")
    private EventType eventType;

    /**
     * The ID of the location where the event is held.
     */
    @NotNull(message = "Location ID must not be null.")
    private Long locationId;
}