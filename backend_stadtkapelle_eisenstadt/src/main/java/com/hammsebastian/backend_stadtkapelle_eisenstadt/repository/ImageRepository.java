/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.repository;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    boolean findImageEntitiesByAuthorAndImageUrl(String author, String imageUrl);
    
    List<ImageEntity> findByGallery_Id(Long galleryId);
}
