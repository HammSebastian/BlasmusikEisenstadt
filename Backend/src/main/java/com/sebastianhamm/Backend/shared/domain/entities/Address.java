/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.shared.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;

@Embeddable
public class Address {

    @Column(name = "street", length = 255)
    @Size(max = 255, message = "Street must be at most 255 characters")
    private String street;

    @Column(name = "street_number", length = 20)
    @Size(max = 20, message = "Street number must be at most 20 characters")
    private String streetNumber;

    @Column(name = "postal_code", length = 20)
    @Size(max = 20, message = "Postal code must be at most 20 characters")
    private String postalCode;

    @Column(name = "city", length = 100)
    @Size(max = 100, message = "City must be at most 100 characters")
    private String city;

    @Column(name = "country", length = 100)
    @Size(max = 100, message = "Country must be at most 100 characters")
    private String country;

    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
