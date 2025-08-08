/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LocationRequest {

    @NotNull(message = "Country must not be null")
    private String country;

    @NotNull(message = "Zip code must not be null")
    private String zipCode;

    @NotNull(message = "City must not be null")
    private String city;

    @NotNull(message = "Street must not be null")
    private String street;

    @NotNull(message = "Number must not be null")
    private String number;
}
