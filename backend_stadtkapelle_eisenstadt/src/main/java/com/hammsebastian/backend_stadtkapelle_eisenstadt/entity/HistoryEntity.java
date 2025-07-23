/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Represents a historical topic or chapter (e.g., "Chronicle", "Conductors").
 *
 * This entity serves as a container for multiple sections (SectionEntity),
 * allowing for structured historical content.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

// Lombok annotations
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "sections") // Exclude collection from toString
@EqualsAndHashCode(of = "id")

// JPA annotations
@Entity
@Table(name = "history")
public class HistoryEntity {

    /**
     * The unique identifier for the history entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name or title of the historical chapter (e.g., "Our Conductors"). Mandatory.
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * The list of sections that make up this historical entry.
     * The foreign key 'history_id' will be created in the 'section' table.
     * orphanRemoval=true ensures sections are deleted if they are removed from this list.
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "history_id") // Corrected FK name
    @Builder.Default
    private List<SectionEntity> sections = new ArrayList<>();
}