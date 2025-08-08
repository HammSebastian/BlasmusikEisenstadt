/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Represents a news article or announcement.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/21/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.NewsType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "news")
@EntityListeners(AuditingEntityListener.class)
public class NewsEntity { // Renamed from NewEntity

    /**
     * The unique identifier for the news article.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The title of the news article. Mandatory.
     */
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    /**
     * The main content of the news article. Mandatory, and allows for long text.
     */
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * The URL to an image for the news article. Optional.
     */
    @Column(name = "news_image_url", length = 500)
    private String newsImageUrl;

    /**
     * The type or category of the news. Mandatory.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "news_type", nullable = false)
    private NewsType newsType;

    /**
     * The date relevant to the news content (e.g., event date). Mandatory.
     */
    @Column(name = "news_date", nullable = false)
    private LocalDate date;

    /**
     * A flag to control the visibility of the news article on the website.
     */
    @Column(name = "is_published", nullable = false)
    private boolean isPublished;

    /**
     * The exact timestamp when the news article was created. Automatically set by the database.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}