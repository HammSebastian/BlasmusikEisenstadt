/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.sebastianhamm.Backend.about.domain.repositories;

import com.sebastianhamm.Backend.about.domain.entities.AboutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AboutRepository extends JpaRepository<AboutEntity, Long> {

    Optional<AboutEntity> findById(Long id);
}
