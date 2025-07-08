package at.sebastianhamm.backend.models;

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
 * Entity representing types of gigs.
 * Bidirectional many-to-many relationship with Gig.
 */
@Entity
@Table(name = "gig_types")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GigType {

    /**
     * Primary key identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Enum type representing the gig type.
     * Unique and limited length.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50, unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private EGigs egigs;

    /**
     * Set of gigs associated with this gig type.
     * Lazy loaded for performance.
     */
    @ManyToMany(mappedBy = "gigTypes", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Gig> gigs = new HashSet<>();

    /**
     * Constructor with required enum field.
     *
     * @param egigs enum value representing gig type
     */
    public GigType(EGigs egigs) {
        this.egigs = egigs;
        this.gigs = new HashSet<>();
    }

    /**
     * Adds a gig maintaining bidirectional consistency.
     *
     * @param gig the gig to add
     */
    public void addGig(Gig gig) {
        this.gigs.add(gig);
        gig.getGigTypes().add(this);
    }

    /**
     * Removes a gig maintaining bidirectional consistency.
     *
     * @param gig the gig to remove
     */
    public void removeGig(Gig gig) {
        this.gigs.remove(gig);
        gig.getGigTypes().remove(this);
    }
}
