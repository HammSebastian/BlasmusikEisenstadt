/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Represents a physical address or location.
 *
 * This entity stores detailed address information and can be associated with other
 * entities, such as events (EventEntity).
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;

import jakarta.persistence.*;
import lombok.*;
// import java.util.Set; // Uncomment if using the bidirectional @OneToMany relationship

// Lombok annotations
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")

// JPA annotations
@Entity
@Table(name = "locations")
public class LocationEntity {

    /**
     * The unique identifier for the location.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The country of the location. Mandatory.
     */
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    /**
     * The postal or ZIP code. Mandatory.
     */
    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

    /**
     * The city or town. Mandatory.
     */
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    /**
     * The street name. Mandatory.
     */
    @Column(name = "street", nullable = false, length = 255)
    private String street;

    /**
     * The street number, including any apartment or suite numbers. Mandatory.
     */
    @Column(name = "street_number", nullable = false, length = 20)
    private String number;

    /*
    // Optional: Uncomment to make the relationship with EventEntity bidirectional.
    // This allows you to easily access all events for a given location.
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EventEntity> events;
    */
}