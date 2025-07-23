/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Represents an event, such as a concert or a public appearance.
 *
 * Each event has a title, date, description, and an associated location. It is a
 * core entity for displaying upcoming or past activities.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.EventType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

// Lombok annotations
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")

// JPA annotations
@Entity
@Table(name = "events")
public class EventEntity {

    /**
     * The unique identifier for the event.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The title of the event. Mandatory.
     */
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    /**
     * A detailed description of the event. Optional.
     */
    @Column(name = "description", length = 2000)
    private String description;

    /**
     * The date on which the event takes place. Mandatory.
     */
    @Column(name = "date", nullable = false)
    private LocalDate date;

    /**
     * The URL to an image representing the event. Optional.
     */
    @Column(name = "event_image_url", length = 500)
    private String eventImageUrl;

    /**
     * The type of the event (e.g., Concert, Rehearsal). Stored as a string in the database.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    /**
     * The location where the event is held. The relationship is loaded lazily for performance.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;
}