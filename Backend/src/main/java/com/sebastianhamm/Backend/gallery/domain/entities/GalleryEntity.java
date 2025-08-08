/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.gallery.domain.entities;
import com.sebastianhamm.Backend.image.api.dtos.ImageResponse;
import com.sebastianhamm.Backend.image.domain.entities.ImageEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a photo gallery for events or occasions.
 *
 * <p>This entity contains metadata about the gallery such as:
 * - Title and date (required for organization)
 * - List of associated images
 *
 * <p><b>Data Protection:</b>
 * - Only metadata is stored; actual image files are stored externally (e.g. CDN or file server)
 * - Access to gallery data must be role-restricted (e.g. Admin, Reporter, Conductor)
 * - Created, updated, and deleted timestamps support audit and soft-deletion (Art. 5, 17 DSGVO)
 *
 * <p><b>Security:</b>
 * - Access via HTTPS only
 * - Changes are audited using Envers
 */
@Audited(withModifiedFlag = true)
@Entity
@Table(name = "gallery")
@SQLDelete(sql = "UPDATE gallery SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@EntityListeners(AuditingEntityListener.class)
public class GalleryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title must not be null")
    @Column(name = "title", nullable = false, length = 255, unique = true)
    private String title;

    @NotNull(message = "Gallery date must not be null")
    @Column(name = "gallery_date", nullable = false)
    private LocalDate galleryDate;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ImageEntity> images = new ArrayList<>();

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "slug", unique = true, length = 255)
    private String slug;

    public GalleryEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getGalleryDate() {
        return galleryDate;
    }

    public List<ImageEntity> getImages() {
        return images;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public String getSlug() {
        return slug;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGalleryDate(LocalDate galleryDate) {
        this.galleryDate = galleryDate;
    }

    public void setImages(List<ImageEntity> images) {
        this.images = images;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @PrePersist
    @PreUpdate
    public void prepareSlug() {
        this.slug = toSlug(this.title);
    }

    private String toSlug(String input) {
        if (input == null) return null;
        return input.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")  // nur a-z, 0-9, Leerzeichen und Bindestrich
                .trim()
                .replaceAll("\\s+", "-");         // Leerzeichen zu Bindestrich
    }
}