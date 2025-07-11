package at.sebastianhamm.backend.entity;

import at.sebastianhamm.backend.enums.ParticipationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Teilnehmer
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // Entweder Gig ...
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gig_id")
    private Gig gig;

    // ... oder Rehearsal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rehearsal_id")
    private Rehearsal rehearsal;

    // Teilnahme-Status: YES / NO / MAYBE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipationStatus status;

    // Grund für Absage oder Vielleicht
    private String reason;

    private LocalDateTime respondedAt;
}
