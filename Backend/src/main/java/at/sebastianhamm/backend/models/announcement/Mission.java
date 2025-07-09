package at.sebastianhamm.backend.models.announcement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents a specific mission or objective related to the "About Us" section.
 * Each mission has a title, description, and an associated image.
 * It is part of a larger "About" entity.
 */
@Entity
@Table(name = "missions")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Mission {

    /**
     * Version field for optimistic locking, used by JPA.
     */
    @Version
    private Long version;

    /**
     * Unique identifier for the mission.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the mission. Must not be blank.
     */
    @NotBlank
    @Column(nullable = false)
    private String title;

    /**
     * Description of the mission. Must not be blank.
     */
    @NotBlank
    @Column(nullable = false)
    private String description;

    /**
     * URL of the image associated with the mission. Must not be blank.
     */
    @NotBlank
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    /**
     * The "About" entity to which this mission belongs.
     * Represents a many-to-one relationship.
     * Uses JsonBackReference to prevent infinite recursion during JSON serialization.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "about_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private About about;

    /**
     * Helper method to properly set the bidirectional relationship with the About entity.
     * Ensures consistency on both sides of the relationship.
     *
     * @param about The About entity to link this mission to.
     */
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
}