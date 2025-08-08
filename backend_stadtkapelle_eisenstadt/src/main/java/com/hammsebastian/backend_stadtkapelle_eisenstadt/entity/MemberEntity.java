/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Represents a member of the music association.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.embeddable.Address;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.InstrumentEnum;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.SectionEnum;
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
@Table(name = "members")
@EntityListeners(AuditingEntityListener.class)
public class MemberEntity {

    /**
     * The unique identifier for the member.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The first name of the member. Mandatory.
     */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName; //

    /**
     * The last name of the member. Mandatory.
     */
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName; //

    /**
     * The primary instrument the member plays. Mandatory.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "instrument", nullable = false)
    private InstrumentEnum instrument; //

    /**
     * The URL to the member's profile picture. Optional.
     */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl; //

    /**
     * The date the member joined the association. Mandatory.
     */
    @Column(name = "date_joined", nullable = false)
    private LocalDate dateJoined; //

    /**
     * The musical section the member belongs to. Mandatory.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "section", nullable = false)
    private SectionEnum section; //

    // --------------------------------| Optional Fields |----------------------------------------
    /**
     * Internal notes about the member. Optional.
     */
    @Column(name = "notes", length = 2000)
    private String notes; //

    /**
     * The member's date of birth. Optional and sensitive.
     */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth; //

    /**
     * The member's phone number. Optional and sensitive.
     */
    @Column(name = "phone_number", length = 30)
    private String phoneNumber; //

    /**
     * The member's physical address, stored as an embedded component.
     */
    @Embedded
    private Address address;
}