package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //------------------| Required |------------------//
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(nullable = false)
    private boolean emailNotification;

    //------------------| Optional |------------------//
    private String avatarUrl;

    // Changeable via put request via profile section in frontend
    private String firstName;

    // Changeable via put request via profile section in frontend
    private String lastName;


    @CurrentTimestamp
    @Column(updatable = false)
    private LocalDate createdAt;

    private LocalDate updatedAt;
}
