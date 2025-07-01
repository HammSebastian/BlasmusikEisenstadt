package at.sebastianhamm.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

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

    private String name;
    private String url;
    private String description;
    private String tookAt;

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
        if (tookAt == null || tookAt.equals(new Date().toString())) {
            throw new IllegalArgumentException("Took at is required");
        }
    }
}
