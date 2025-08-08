/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 8/8/25
 */
package com.sebastianhamm.Backend.location.api.dtos;

import com.sebastianhamm.Backend.shared.api.dtos.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LocationDto {

    private Long id;

    @NotNull(message = "Name must not be null")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Valid
    private AddressDto address;

    public LocationDto() {
    }

    public LocationDto(Long id, String name, AddressDto address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}