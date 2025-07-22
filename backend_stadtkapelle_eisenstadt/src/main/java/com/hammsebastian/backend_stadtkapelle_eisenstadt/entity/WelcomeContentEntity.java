/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "welcome_content")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WelcomeContentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Erhöhen Sie die Länge für Felder, die längere Texte enthalten könnten
    @Column(length = 500) // Beispiel: Erlaubt bis zu 500 Zeichen für den Titel
    private String title;

    @Column(length = 1000) // Beispiel: Erlaubt bis zu 1000 Zeichen für den Untertitel
    private String subTitle;

    @Column(length = 255) // Button-Text ist wahrscheinlich selten länger als 255
    private String buttonText;

    @Column(length = 500) // Beispiel: Erlaubt bis zu 500 Zeichen für den Bildpfad/URL
    private String backgroundImage;
}
