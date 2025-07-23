/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/21/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.NewsType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Column(name = "news_image")
    private String newsImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "news_type")
    private NewsType newsType;

    private LocalDate date;

    @Column(name = "is_published")
    private boolean isPublished;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
