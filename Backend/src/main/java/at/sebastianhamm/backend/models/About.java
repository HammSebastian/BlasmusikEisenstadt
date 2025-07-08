package at.sebastianhamm.backend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "about", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class About {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String imageUrl;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String story;

    @OneToMany(mappedBy = "about", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Mission> missions = new HashSet<>();


    // Helper methods for bidirectional relationship
    public void addMission(Mission mission) {
        if (mission != null) {
            this.missions.add(mission);
            mission.setAbout(this);
        }
    }

    public void removeMission(Mission mission) {
        if (mission != null) {
            missions.remove(mission);
            mission.setAbout(null);
        }
    }
    
    // Initialize missions if null to prevent NPE
    public Set<Mission> getMissions() {
        if (missions == null) {
            missions = new HashSet<>();
        }
        return missions;
    }
}
