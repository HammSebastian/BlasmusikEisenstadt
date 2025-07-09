package at.sebastianhamm.backend.models.gig;

import at.sebastianhamm.backend.models.gig.enums.EGigs;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing types or categories of gigs.
 * Establishes a bidirectional many-to-many relationship with Gig entities.
 */
@Entity
@Table(name = "gig_types")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GigType {

    /**
     * Unique identifier for the gig type.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Enum type representing the specific gig classification (e.g., BRUNCH, CONCERT).
     * Stored as a string in the database and must be unique.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50, unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private EGigs egigs; // **Vorschlag: Umbenennung zu EGigCategory oder EGigTypeEnum**

    /**
     * Set of Gig entities associated with this gig type.
     * Mapped by "gigTypes" in the Gig entity and lazily loaded for performance.
     * Uses JsonIdentityInfo for proper serialization in bidirectional relationships.
     */
    @ManyToMany(mappedBy = "gigTypes", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Gig> gigs = new HashSet<>();

    /**
     * Constructor with the required enum field.
     *
     * @param egigs The EGigs enum value representing the gig type.
     */
    public GigType(EGigs egigs) {
        this.egigs = egigs;
        this.gigs = new HashSet<>();
    }

    /**
     * Adds a gig to the set, maintaining bidirectional consistency.
     *
     * @param gig The Gig entity to add.
     */
    public void addGig(Gig gig) {
        this.gigs.add(gig);
        gig.getGigTypes().add(this);
    }

    /**
     * Removes a gig from the set, maintaining bidirectional consistency.
     *
     * @param gig The Gig entity to remove.
     */
    public void removeGig(Gig gig) {
        this.gigs.remove(gig);
        gig.getGigTypes().remove(this);
    }
}