package at.sebastianhamm.backend.models.gig;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gigs")
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Gig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    /**
     * Set of types associated with this gig (e.g., BRUNCH, CONCERT).
     * Manages the many-to-many relationship with GigType entities.
     * Uses JsonIdentityInfo for handling bidirectional serialization.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "gig_gig_types",
            joinColumns = @JoinColumn(name = "gig_id"),
            inverseJoinColumns = @JoinColumn(name = "gigtype_id"))
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<GigType> gigTypes = new HashSet<>();

    /**
     * Helper method to add a GigType, maintaining bidirectional consistency.
     *
     * @param gigType The GigType to add.
     */
    public void addGigType(GigType gigType) {
        this.gigTypes.add(gigType);
        gigType.getGigs().add(this);
    }

    /**
     * Helper method to remove a GigType, maintaining bidirectional consistency.
     *
     * @param gigType The GigType to remove.
     */
    public void removeGigType(GigType gigType) {
        this.gigTypes.remove(gigType);
        gigType.getGigs().remove(this);
    }

    @NotBlank
    private String venue;

    @NotBlank
    private String address;

    @NotBlank
    private String imageUrl;

    private LocalDate date;

    private LocalTime time;

    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    @PrePersist
    public void prePersist() {
        if (date == null) {
            date = LocalDate.now();
        }
        if (time == null) {
            time = LocalTime.now();
        }
    }
}
