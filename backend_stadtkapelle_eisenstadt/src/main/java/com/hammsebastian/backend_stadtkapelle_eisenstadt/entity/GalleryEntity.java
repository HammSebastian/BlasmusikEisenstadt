/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Represents a photo gallery, which is a collection of images.
 *
 * Each gallery has a unique title and a date, and it contains a list of ImageEntity objects.
 * The lifecycle of the images is cascaded from the gallery.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Lombok annotations
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "images") // Exclude collection from toString to avoid performance issues
@EqualsAndHashCode(of = "id")

// JPA annotations
@Entity
@Table(name = "gallery")
public class GalleryEntity {

    /**
     * The unique identifier for the gallery.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique title of the gallery. Mandatory.
     */
    @Column(name = "title", unique = true, nullable = false, length = 255)
    private String title;

    /**
     * The primary date associated with the gallery (e.g., event date). Mandatory.
     */
    @Column(name = "gallery_date", nullable = false)
    private LocalDate galleryDate;

    /**
     * The list of images belonging to this gallery.
     * Operations on the gallery (like deletion) are cascaded to the images.
     * The foreign key 'gallery_id' will be created in the 'images' table.
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "gallery_id")
    @Builder.Default
    private List<ImageEntity> images = new ArrayList<>();
}