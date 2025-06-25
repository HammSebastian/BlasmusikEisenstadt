/**
 * @author Sebastian Hamm
 * @filename Image.java
 * @created 6/23/25, Monday
 */

package at.sebastianhamm.kapelle_eisenstadt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "image_path")
    private String imagePath;

    @Column(nullable = false, name = "image_category")
    private String imageCategory;

    @Column(nullable = false, name = "image_author")
    private String imageAuthor;
}
