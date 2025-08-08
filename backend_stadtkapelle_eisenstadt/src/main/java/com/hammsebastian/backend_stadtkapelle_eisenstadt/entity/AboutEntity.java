/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * <p>
 * Represents the content for an "About Us" page or section of the website.
 * <p>
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "about")
@EntityListeners(AuditingEntityListener.class)
public class AboutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "About text must not be blank")
    @Size(max = 5000, message = "About text must be max 5000 characters")
    @Lob
    @Column(name = "about_text", nullable = false, columnDefinition = "TEXT")
    private String aboutText;

    @Size(max = 500, message = "Image URL must be max 500 characters")
    @Column(name = "about_image_url", length = 500)
    private String aboutImageUrl;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
