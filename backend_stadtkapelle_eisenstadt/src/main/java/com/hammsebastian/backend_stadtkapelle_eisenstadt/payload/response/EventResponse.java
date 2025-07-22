/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.EventEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private String eventImage;
    private String eventType;
    private LocationResponse location;

    public static EventResponse toEventResponse(EventEntity eventEntity) {
        return EventResponse.builder()
                .id(eventEntity.getId())
                .title(eventEntity.getTitle())
                .description(eventEntity.getDescription())
                .date(eventEntity.getDate())
                .eventImage(eventEntity.getEventImage())
                .eventType(eventEntity.getEventType().toString())
                .location(LocationResponse.toLocationResponse(eventEntity.getLocation()))
                .build();
    }
}
