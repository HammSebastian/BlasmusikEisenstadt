/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.InstrumentEnum;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.SectionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "members")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MemberEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "instrument")
    private InstrumentEnum instrument;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "date_joined")
    private LocalDate dateJoined;

    @Enumerated(EnumType.STRING)
    @Column(name = "section")
    private SectionEnum section;

    //--------------------------------| Optional Fields |----------------------------------------
    private String notes;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String number;
    private String street;
    private String zipCode;
    private String city;
    private String country;
    //-------------------------------------------------------------------------------------------
}
