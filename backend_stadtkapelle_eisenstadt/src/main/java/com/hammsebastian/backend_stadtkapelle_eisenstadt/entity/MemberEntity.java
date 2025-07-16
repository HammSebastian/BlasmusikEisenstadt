package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.RoleEnum;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "members")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //------------------| Required |------------------//
    @Column(unique = true, nullable = false)
    @NotNull(message = "Email is required")
    private String email;

    @Column(nullable = false)
    @NotNull(message = "Password is required")
    private String password;

    @Column(nullable = false)
    @NotNull(message = "Firstname is required")
    private String firstName;

    @Column(nullable = false)
    @NotNull(message = "Lastname is required")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Role is required")
    private RoleEnum role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    @NotNull(message = "Created by is required")
    private MemberEntity createdBy;

    @Column(nullable = false)
    @NotNull(message = "Avatar URL is required")
    private String avatarUrl;

    @Column(nullable = false)
    @NotNull(message = "Email notification is required")
    private String emailNotification;

    //------------------| Optional |------------------//
    private LocalDate createdAt;
    private LocalDate updatedAt;


    //------------------| Init |------------------//
    @PostConstruct
    public void init() {
        createdAt = LocalDate.now();
    }
}
