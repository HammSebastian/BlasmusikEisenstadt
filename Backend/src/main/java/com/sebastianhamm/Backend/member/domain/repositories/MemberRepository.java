/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.member.domain.repositories;

import com.sebastianhamm.Backend.member.domain.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    List<MemberEntity> findAllByDeletedAtIsNull();

    // Optional: findById nur für nicht gelöschte Entities
    default Optional<MemberEntity> findActiveById(Long id) {
        return findById(id).filter(m -> m.getDeletedAt() == null);
    }
}
