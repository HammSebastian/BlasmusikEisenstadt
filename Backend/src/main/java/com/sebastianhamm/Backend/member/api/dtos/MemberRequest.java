/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.member.api.dtos;

import com.sebastianhamm.Backend.member.domain.enums.InstrumentEnum;
import com.sebastianhamm.Backend.member.domain.enums.SectionEnum;
import com.sebastianhamm.Backend.shared.api.dtos.AddressDto;
import com.sebastianhamm.Backend.member.domain.validation.AvatarValidation;
import com.sebastianhamm.Backend.member.domain.validation.InstrumentValidation;
import com.sebastianhamm.Backend.member.domain.validation.SectionValidation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

public class MemberRequest {

    @NotNull(message = "First name must not be null")
    @Pattern(
            regexp = "^[\\p{L} \\-']{2,100}$",
            message = "First name must only contain letters, spaces, hyphens, apostrophes"
    )
    private String firstName;

    @NotNull(message = "Last name must not be null")
    @Pattern(
            regexp = "^[\\p{L} \\-']{2,100}$",
            message = "Last name must only contain letters, spaces, hyphens, apostrophes"
    )
    private String lastName;

    @NotNull(message = "Instrument must not be null")
    @InstrumentValidation
    private InstrumentEnum instrument;

    @NotNull(message = "Avatar URL must not be null")
    @URL(message = "Avatar URL must be a valid URL")
    @AvatarValidation
    private String avatarUrl;

    @NotNull(message = "Date joined must not be null")
    @PastOrPresent(message = "Date joined cannot be in the future")
    private LocalDate dateJoined;

    @NotNull(message = "Section must not be null")
    @SectionValidation
    private SectionEnum section;

    // Optional
    private String notes;

    @Pattern(regexp = "^[+]?\\d{7,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    private AddressDto address;

    public MemberRequest() {
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

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}
