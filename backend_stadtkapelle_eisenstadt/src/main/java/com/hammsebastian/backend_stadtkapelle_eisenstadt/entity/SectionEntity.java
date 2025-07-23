/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Represents a specific entry or paragraph within a larger historical context (HistoryEntity).
 * Each section is typically associated with a year and contains descriptive text.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;

import jakarta.persistence.*;
import lombok.*;

// Lombok annotations
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "history") // Exclude parent relationship to prevent recursion
@EqualsAndHashCode(of = "id")

// JPA annotations
@Entity
@Table(name = "sections")
public class SectionEntity {

    /**
     * The unique identifier for the section.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The year associated with this historical section. Mandatory.
     */
    @Column(name = "year", nullable = false)
    private int year;

    /**
     * The descriptive text for this section. Mandatory and supports long text.
     */
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * A reference to the parent HistoryEntity this section belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id", nullable = false)
    private HistoryEntity history;
}