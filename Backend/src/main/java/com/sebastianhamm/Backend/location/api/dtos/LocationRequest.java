/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.location.api.dtos;
import com.sebastianhamm.Backend.event.domain.entities.EventEntity;

import com.sebastianhamm.Backend.shared.domain.entities.Address;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;

public class LocationRequest {

    @NotNull(message = "Name must not be null")
    private String name;

    @Embedded
    private Address address;

    public LocationRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
