/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.repository;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.GalleryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface GalleryRepository extends JpaRepository<GalleryEntity, Long> {
    boolean findByTitleAndFromDate(String title, LocalDate fromDate);

    Optional<GalleryEntity> findGalleryEntityByTitle(String title);
}
