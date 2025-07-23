/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Represents the dynamic content for the main "Welcome" or "Hero" section of the homepage.
 *
 * This entity typically holds a single record that an administrator can update
 * to change the site's initial landing message.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/20/25
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
@ToString
@EqualsAndHashCode(of = "id")

// JPA annotations
@Entity
@Table(name = "welcome")
public class WelcomeEntity {

    /**
     * The unique identifier for the welcome content entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The main headline text. Mandatory.
     */
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    /**
     * The supporting subtitle text displayed below the main title. Mandatory.
     */
    @Column(name = "subtitle", nullable = false, length = 1000)
    private String subtitle; // Corrected to camelCase

    /**
     * The text for the call-to-action button. Mandatory.
     */
    @Column(name = "button_text", nullable = false, length = 255)
    private String buttonText;

    /**
     * The URL for the background image of the welcome section. Mandatory.
     */
    @Column(name = "background_image_url", nullable = false, length = 500)
    private String backgroundImageUrl;
}