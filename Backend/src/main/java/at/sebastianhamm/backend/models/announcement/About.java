package at.sebastianhamm.backend.models.announcement;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the "About Us" section content of the application.
 * This entity stores static information like the company's story,
 * image URL, and a collection of missions.
 */
@Entity
@Table(name = "about", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class About {
    /**
     * Unique identifier for the About section.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * URL of the image associated with the About section.
     * Must not be blank.
     */
    @NotBlank
    @Column(nullable = false)
    private String imageUrl;

    /**
     * The main story or description of the About section.
     * Stored as TEXT to accommodate longer content. Must not be blank.
     */
    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String story;

    /**
     * A collection of missions associated with this About section.
     * Manages the bidirectional relationship with Mission entities.
     * All associated missions will be cascaded on persistence, merge,
     * remove, refresh, and detach operations. Orphan removal is enabled.
     */
    @OneToMany(mappedBy = "about", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Mission> missions = new HashSet<>();


    /**
     * Adds a mission to the set, maintaining bidirectional consistency.
     *
     * @param mission The Mission entity to add.
     */
    public void addMission(Mission mission) {
        if (mission != null) {
            this.missions.add(mission);
            mission.setAbout(this);
        }
    }

    /**
     * Removes a mission from the set, maintaining bidirectional consistency.
     *
     * @param mission The Mission entity to remove.
     */
    public void removeMission(Mission mission) {
        if (mission != null) {
            missions.remove(mission);
            mission.setAbout(null);
        }
    }

    /**
     * Retrieves the set of missions. Initializes the set if it's null to prevent NullPointerExceptions.
     *
     * @return The set of missions.
     */
    public Set<Mission> getMissions() {
        if (missions == null) {
            missions = new HashSet<>();
        }
        return missions;
    }
}