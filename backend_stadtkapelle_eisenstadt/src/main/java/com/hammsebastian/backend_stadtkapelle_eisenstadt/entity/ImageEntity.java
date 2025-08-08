/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * <p>
 * Represents a single image, which is part of a gallery.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "images")
@EntityListeners(AuditingEntityListener.class)
public class ImageEntity {

    /**
     * The unique identifier for the image.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The URL where the image is stored. This field is mandatory.
     */
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl; //

    /**
     * The name of the photographer or copyright holder. Optional.
     */
    @Column(name = "author", length = 255)
    private String author; //

    /**
     * The date the image was created or uploaded. Optional.
     */
    @Column(name = "upload_date")
    private LocalDate date; //

    /**
     * A reference to the gallery this image belongs to. Loaded lazily for performance.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery_id", updatable = false) // FK should not be updated directly
    private GalleryEntity gallery; //
}