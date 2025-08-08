/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.image.domain.entities;
import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;

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

/**
 * Represents an uploaded image with metadata.
 *
 * <p>This entity stores:
 * - Image URL referencing the stored file (no binary data stored here)
 * - Author information (mandatory for accountability)
 * - Upload date and audit timestamps
 * - Metadata: filename, mime type, file size for validation and security checks
 *
 * <p><b>Data Protection Considerations:</b>
 * - Ensure user consent for any identifiable images (Art. 6, 7 DSGVO)
 * - Store only metadata in the DB; image files handled securely outside DB
 * - Soft-deletion allows complying with data erasure requests (Art. 17 DSGVO)
 * - Audit logs via Envers to track changes without exposing sensitive data
 *
 * <p><b>Security:</b>
 * - Validate MIME type, file size and filename before persistence
 * - Restrict upload and access to authorized roles only
 * - All access and modification must happen over encrypted channels (HTTPS)
 */

@Audited(withModifiedFlag = true)
@Entity
@Table(name = "images")
@SQLDelete(sql = "UPDATE images SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@EntityListeners(AuditingEntityListener.class)
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Image URL must not be null")
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @NotNull(message = "Author must not be null")
    @Column(name = "author", nullable = false, length = 255)
    private String author;

    @Column(name = "upload_date")
    @NotNull(message = "Upload date must not be null")
    private LocalDate uploadDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery_id", updatable = false)
    private GalleryEntity gallery;

    @Column(name = "filename", length = 255)
    private String filename;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(unique = true)
    private String slug;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public ImageEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public GalleryEntity getGallery() {
        return gallery;
    }

    public void setGallery(GalleryEntity gallery) {
        this.gallery = gallery;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(Long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
