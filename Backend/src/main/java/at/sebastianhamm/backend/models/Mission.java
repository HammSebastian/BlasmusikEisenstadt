package at.sebastianhamm.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "missions")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Mission {

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotBlank
    @Column(name = "image_url", nullable = false)
    private String imageUrl;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "about_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private About about;
    
    // Helper method to properly set the about relationship
    public void setAbout(About about) {
        // Prevent infinite loop
        if (this.about == about) {
            return;
        }
        
        // Unlink from the old about if exists
        if (this.about != null) {
            this.about.getMissions().remove(this);
        }
        
        // Set the new about
        this.about = about;
        
        // Update the relationship on the other side
        if (about != null) {
            about.getMissions().add(this);
        }
    }
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mission)) return false;
        Mission mission = (Mission) o;
        if (id != null && mission.id != null) {
            return id.equals(mission.id);
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return System.identityHashCode(this);
    }



 */
}
