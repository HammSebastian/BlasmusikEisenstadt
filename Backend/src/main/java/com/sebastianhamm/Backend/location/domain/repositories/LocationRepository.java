/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.location.domain.repositories;
import com.sebastianhamm.Backend.event.domain.entities.EventEntity;

import com.sebastianhamm.Backend.location.domain.entities.LocationEntity;
import com.sebastianhamm.Backend.shared.domain.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {
    Optional<LocationEntity> findByName(String name);

    Optional<List<LocationEntity>> getLocationEntitiesByName(String name);

    Optional<LocationEntity> findById(Long id);

    Optional<LocationEntity> getLocationEntityByAddress(Address address);
}
