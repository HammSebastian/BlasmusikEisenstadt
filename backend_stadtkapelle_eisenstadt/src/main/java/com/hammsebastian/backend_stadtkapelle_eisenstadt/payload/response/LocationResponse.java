/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.LocationEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LocationResponse {
    private Long id;
    private String country;
    private String zipCode;
    private String city;
    private String street;
    private String number;

    public static LocationResponse toLocationResponse(LocationEntity locationEntity) {
        return LocationResponse.builder()
                .id(locationEntity.getId())
                .country(locationEntity.getCountry())
                .zipCode(locationEntity.getZipCode())
                .city(locationEntity.getCity())
                .street(locationEntity.getStreet())
                .number(locationEntity.getNumber())
                .build();
    }
}
