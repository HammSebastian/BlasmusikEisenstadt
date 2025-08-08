/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * <p>
 * Represents a historical topic or chapter (e.g., "Chronicle", "Conductors").
 * <p>
 * This entity serves as a container for multiple sections (SectionEntity),
 * allowing for structured historical content.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "history", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@EntityListeners(AuditingEntityListener.class)
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "History name must not be blank")
    @Size(min = 2, max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SectionEntity> sections = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    private OffsetDateTime updatedAt;

    public void addSection(SectionEntity section) {
        sections.add(section);
        section.setHistory(this);
    }

    public void removeSection(SectionEntity section) {
        sections.remove(section);
        section.setHistory(null);
    }
}
