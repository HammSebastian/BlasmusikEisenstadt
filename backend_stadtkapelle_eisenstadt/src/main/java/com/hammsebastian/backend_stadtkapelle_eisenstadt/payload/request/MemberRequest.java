/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.embeddable.Address;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.InstrumentEnum;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.SectionEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class MemberRequest {

    @NotNull(message = "First name must not be null")
    private String firstName;

    @NotNull(message = "Last name must not be null")
    private String lastName;

    @NotNull(message = "Instrument must not be blank")
    private InstrumentEnum instrument;

    @NotBlank(message = "Avatar URL must not be blank")
    private String avatarUrl;

    @NotNull(message = "Date joined must not be null")
    private LocalDate dateJoined;

    @NotNull(message = "Section must not be null")
    private SectionEnum section;

    //--------------------------------| Optional Fields |----------------------------------------
    private String notes;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private Address address;
    //-------------------------------------------------------------------------------------------
}
