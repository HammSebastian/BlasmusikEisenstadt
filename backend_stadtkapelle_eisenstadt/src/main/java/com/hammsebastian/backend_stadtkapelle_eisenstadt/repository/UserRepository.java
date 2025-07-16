package com.hammsebastian.backend_stadtkapelle_eisenstadt.repository;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
