/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/29/25
 */
package com.sebastianhamm.Backend.location.api.dtos;
import com.sebastianhamm.Backend.event.domain.entities.EventEntity;

import com.sebastianhamm.Backend.shared.domain.entities.Address;
import jakarta.persistence.Embedded;

public class LocationResponse {

    private String name;

    @Embedded
    private Address address;

    public LocationResponse() {
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
