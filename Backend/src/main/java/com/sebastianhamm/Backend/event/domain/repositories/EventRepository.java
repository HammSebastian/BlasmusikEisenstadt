/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.event.domain.repositories;
import com.sebastianhamm.Backend.location.domain.entities.LocationEntity;

import com.sebastianhamm.Backend.event.domain.entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    Optional<EventEntity> findByTitle(String title);
    Optional<EventEntity> findEventEntityByLocation(LocationEntity locationEntity);
    Optional<EventEntity> findEventEntityByTitle(String title);
}
