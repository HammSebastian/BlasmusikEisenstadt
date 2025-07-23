/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Represents the content for an "About Us" page or section of the website.
 *
 * This entity stores the main descriptive text and a URL to an associated image.
 * It is designed to be managed through an admin interface to update the public-facing
 * information about the organization.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;

import jakarta.persistence.*;
import lombok.*;

// Lombok annotations for boilerplate code generation
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id") // Note: Equality is based on the database ID. New, un-persisted entities will not be equal.

// JPA annotations for persistence
@Entity
@Table(name = "about")
public class AboutEntity {

    /**
     * The unique identifier for the entity.
     * It is generated automatically by the database using an identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The main descriptive text for the "About Us" section.
     * This field is mandatory and has a maximum length of 1000 characters.
     */
    @Column(name = "about_text", nullable = false, length = 1000)
    private String aboutText;

    /**
     * The URL of the primary image for the "About Us" section.
     * This field is optional and has a maximum length of 500 characters.
     * Storing a URL is preferred over storing image blobs directly in the database.
     */
    @Column(name = "about_image_url", length = 500)
    private String aboutImageUrl;
}