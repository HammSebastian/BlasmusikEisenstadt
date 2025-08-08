/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.about.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Audited(withModifiedFlag = true)
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
    @Column(name = "about_image_url", length = 500, nullable = false)
    private String aboutImageUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public AboutEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getAboutText() {
        return aboutText;
    }

    public String getAboutImageUrl() {
        return aboutImageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAboutText(String aboutText) {
        this.aboutText = aboutText;
    }

    public void setAboutImageUrl(String aboutImageUrl) {
        this.aboutImageUrl = aboutImageUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
