package at.sebastianhamm.backend.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.sql.Time;
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "gig_gig_types",
            joinColumns = @JoinColumn(name = "gig_id"),
            inverseJoinColumns = @JoinColumn(name = "gigtype_id"))
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<GigType> gigTypes = new HashSet<>();

    // Helper methods for bidirectional relationship
    public void addGigType(GigType gigType) {
        this.gigTypes.add(gigType);
        gigType.getGigs().add(this);
    }

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

    @NotBlank
    private String note;

    private Date date;

    private Time time;

    @NotBlank
    private String createdBy;
}