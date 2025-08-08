/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.welcome.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


/**
 * Represents the welcome section content of the public landing page.
 *
 * <p>This entity does NOT contain personal data under Art. 4 Nr. 1 DSGVO.
 * Indirect references to personal data (e.g., images) must comply with consent rules.
 *
 * <p><b>Security & Compliance:</b>
 * - All text fields must be sanitized before output to prevent XSS attacks.
 * - Background images must not contain identifiable persons without documented consent.
 * - Edits must be restricted to authorized roles only (RBAC in service/controller).
 * - Audit trail is enabled with Hibernate Envers to ensure traceability.
 * - Transport must always be secured via TLS (HTTPS).
 * - Background images served only from trusted, secure sources (avoid mixed content).
 *
 * <p><b>DSGVO Principles Applied:</b>
 * - Purpose limitation: Public display with legitimate interest (Art. 6 Abs. 1 lit. f).
 * - Data minimization: No storage of sensitive or personal data.
 * - Storage limitation: Audit logs must have retention policies.
 * - Integrity & confidentiality: Access controls and secure transport.
 */


@Audited(withModifiedFlag = true)
@Entity
@Table(name = "welcome")
@EntityListeners(AuditingEntityListener.class)
public class WelcomeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title must not be null")
    @Column(nullable = false, length = 500)
    private String title;

    @NotNull(message = "Subtitle must not be null")
    @Column(nullable = false, length = 1000)
    private String subTitle;

    @NotNull(message = "Button text must not be null")
    @Column(name = "button_text", nullable = false, length = 255)
    private String buttonText;

    @NotNull(message = "Background image must not be null")
    @Column(name = "background_image_url", nullable = false, length = 500)
    private String backgroundImageUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public WelcomeEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}