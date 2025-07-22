/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.repository;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.MemberEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.InstrumentEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsMemberEntityByFirstNameAndLastNameAndInstrument(String firstName, String lastName, InstrumentEnum instrument);

    Integer countAllByAvatarUrl(String avatarUrl);

    Integer countMemberEntitiesById(Long id);
}
