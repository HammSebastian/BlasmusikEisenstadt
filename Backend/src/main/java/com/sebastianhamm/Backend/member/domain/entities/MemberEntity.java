/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.member.domain.entities;

import com.sebastianhamm.Backend.shared.domain.entities.Address;
import com.sebastianhamm.Backend.member.domain.enums.InstrumentEnum;
import com.sebastianhamm.Backend.member.domain.enums.SectionEnum;
import com.sebastianhamm.Backend.member.domain.validation.AvatarValidation;
import com.sebastianhamm.Backend.member.domain.validation.InstrumentValidation;
import com.sebastianhamm.Backend.member.domain.validation.SectionValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a member of the orchestra.
 *
 * <p>This entity contains personal data (Art. 4 Nr. 1 DSGVO) such as:
 * - Full name (Art. 6 Abs. 1 lit. b DSGVO - contract/membership)
 * - Address, Phone (optional contact data - Art. 6 Abs. 1 lit. a DSGVO - consent)
 * - Instrument, section (necessary for organizational purpose)
 * - Avatar URL (optional, requires consent if image is identifiable)
 * - Notes (must be sanitized to avoid XSS - Art. 32 DSGVO)
 *
 * <p>⚠️ All optional fields should only be persisted with explicit user consent.
 * <p>⚠️ Soft-deletion is implemented to support "Right to be forgotten" (Art. 17 DSGVO).
 *
 * <p>This entity must be handled in compliance with:
 * - Purpose limitation (Art. 5(1)(b) DSGVO)
 * - Data minimization (Art. 5(1)(c) DSGVO)
 * - Storage limitation (Art. 5(1)(e) DSGVO)
 * - Integrity & confidentiality (Art. 5(1)(f) DSGVO)
 *
 * <p><b>Technische Anforderungen:</b>
 * - TLS-Verschlüsselung bei Transport (HTTPS)
 * - Zugriff nur für berechtigte Rollen (RBAC)
 * - Datenbankzugriff auf autorisierte Services beschränkt
 * - Backups müssen verschlüsselt sein
 * - Datenlöschung auf Anfrage muss technisch umsetzbar sein (siehe SoftDelete)
 * - Logging darf keine sensitiven Inhalte (z. B. Notes, Adresse) enthalten
 *
 * <p>Sanitization: Notes field must be sanitized before output (e.g. OWASP Java HTML Sanitizer).
 */

@Audited(withModifiedFlag = true)
@Entity
@Table(name = "members")
@SQLDelete(sql = "UPDATE members SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@EntityListeners(AuditingEntityListener.class)
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Erweitertes Regex: erlaubt Buchstaben, Akzente, Leerzeichen, Bindestriche, Apostroph
    @NotNull(message = "First name must not be null")
    @Pattern(
            regexp = "^[\\p{L} \\-']{2,100}$",
            message = "First name must only contain letters, spaces, hyphens, apostrophes"
    )
    @Column(nullable = false, length = 100, name = "first_name")
    private String firstName;

    @NotNull(message = "Last name must not be null")
    @Pattern(
            regexp = "^[\\p{L} \\-']{2,100}$",
            message = "Last name must only contain letters, spaces, hyphens, apostrophes"
    )
    @Column(nullable = false, length = 100, name = "last_name")
    private String lastName;

    @NotNull(message = "Instrument must not be null")
    @InstrumentValidation
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, name = "instrument")
    private InstrumentEnum instrument;

    @NotNull(message = "Avatar URL must not be null")
    @URL(message = "Avatar URL must be a valid URL")
    @AvatarValidation
    @Column(nullable = false, length = 255)
    private String avatarUrl;

    @NotNull(message = "Date joined must not be null")
    @Column(nullable = false, name = "date_joined")
    @PastOrPresent
    private LocalDate dateJoined;

    @NotNull(message = "Section must not be null")
    @SectionValidation
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "section", length = 20)
    private SectionEnum section;

    //-----------------------------------| Optional Fields |-----------------------------------
    // Achtung: notes müssen vor Ausgabe immer sanitized werden, z.B. mit OWASP Java HTML Sanitizer
    @Column(columnDefinition = "TEXT")
    private String notes;

    @Pattern(regexp = "^[+]?\\d{7,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    @Embedded
    private Address address;

    //-----------------------------------| Optional Fields |-----------------------------------

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public InstrumentEnum getInstrument() {
        return instrument;
    }

    public void setInstrument(InstrumentEnum instrument) {
        this.instrument = instrument;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public LocalDate getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
    }

    public SectionEnum getSection() {
        return section;
    }

    public void setSection(SectionEnum section) {
        this.section = section;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public MemberEntity() {
    }
}