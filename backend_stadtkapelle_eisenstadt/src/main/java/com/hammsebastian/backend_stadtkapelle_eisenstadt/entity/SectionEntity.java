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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "news")
@EntityListeners(AuditingEntityListener.class)
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
    @NotNull(message = "Year must not be null")
    @Min(value = 1800, message = "Year must be at least 1800")
    @Column(name = "year", nullable = false)
    private int year;

    /**
     * The descriptive text for this section. Mandatory and supports long text.
     */
    @NotBlank(message = "Description must not be blank")
    @Size(min = 10, message = "Description must be at least 10 characters")
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * A reference to the parent HistoryEntity this section belongs to.
     */
    @NotNull(message = "History must not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id", nullable = false)
    private HistoryEntity history;
}