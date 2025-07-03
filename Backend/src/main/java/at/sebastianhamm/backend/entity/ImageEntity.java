package at.sebastianhamm.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_images")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    private String description;

    @Column(name = "took_at", nullable = false)
    private LocalDateTime tookAt;

    @ManyToOne
    @JoinColumn(name = "gig_id")
    private GigEntity gig;

    @ManyToOne
    @JoinColumn(name = "musician_id")
    private MusiciansEntity musician;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity locationEntity;

    @PrePersist
    public void prePersist() {
        if (tookAt == null) {
            tookAt = LocalDateTime.now();
        }
    }
}
