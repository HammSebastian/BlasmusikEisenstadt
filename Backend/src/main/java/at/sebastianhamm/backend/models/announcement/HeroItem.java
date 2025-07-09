package at.sebastianhamm.backend.models.announcement;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a "Hero Item" typically displayed prominently
 * on a website, e.g., on a homepage carousel or banner.
 * It includes a title, description, and an image URL.
 */
@Entity
@Table(name = "hero_items")
@Getter
@Setter
@NoArgsConstructor
public class HeroItem {

    /**
     * Unique identifier for the Hero Item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the hero item. Cannot be blank.
     */
    @Column(nullable = false)
    @NotBlank(message = "Title must not be blank")
    private String title;

    /**
     * Description of the hero item. Stored as TEXT to accommodate longer content.
     * Cannot be blank.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Description must not be blank")
    private String description;

    /**
     * URL of the hero item's image. Cannot be blank.
     */
    @Column(nullable = false)
    @NotBlank(message = "Image URL must not be blank")
    private String imageUrl;
}