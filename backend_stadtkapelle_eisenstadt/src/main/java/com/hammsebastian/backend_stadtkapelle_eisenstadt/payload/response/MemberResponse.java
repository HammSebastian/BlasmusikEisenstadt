/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.MemberEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class MemberResponse {

    private String firstName;
    private String lastName;
    private String instrument;
    private String avatarUrl;
    private LocalDate dateJoined;
    private String section;


    //--------------------------------| Optional Fields |----------------------------------------
    private String notes;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String number;
    private String street;
    private String zipCode;
    private String city;
    private String country;
    //-------------------------------------------------------------------------------------------


    public static MemberResponse toMemberResponse(MemberEntity memberEntity) {
        return MemberResponse.builder()
                .firstName(memberEntity.getFirstName())
                .lastName(memberEntity.getLastName())
                .instrument(memberEntity.getInstrument().toString())
                .avatarUrl(memberEntity.getAvatarUrl())
                .dateJoined(memberEntity.getDateJoined())
                .section(memberEntity.getSection().toString())
                .notes(memberEntity.getNotes())
                .dateOfBirth(memberEntity.getDateOfBirth())
                .phoneNumber(memberEntity.getPhoneNumber())
                .number(memberEntity.getAddress().getNumber())
                .street(memberEntity.getAddress().getStreet())
                .zipCode(memberEntity.getAddress().getZipCode())
                .city(memberEntity.getAddress().getCity())
                .country(memberEntity.getAddress().getCountry())
                .build();
    }
}
