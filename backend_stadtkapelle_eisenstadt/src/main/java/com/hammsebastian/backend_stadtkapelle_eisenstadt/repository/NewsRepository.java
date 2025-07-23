/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/21/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.repository;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    Optional<NewsEntity> findNewsEntityByTitleAndDescription(String title, String description);
}
