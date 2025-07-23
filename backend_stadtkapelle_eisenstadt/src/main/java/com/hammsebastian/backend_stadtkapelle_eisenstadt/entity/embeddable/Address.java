/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */

package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * A reusable component representing a physical address.
 * Using @Embeddable allows this to be cleanly included in other entities.
 */
@Getter
@Setter
@Embeddable
public class Address {

    @Column(name = "street", length = 255)
    private String street;

    @Column(name = "street_number", length = 20)
    private String number;

    @Column(name = "zip_code", length = 20)
    private String zipCode;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "country", length = 100)
    private String country;
}
