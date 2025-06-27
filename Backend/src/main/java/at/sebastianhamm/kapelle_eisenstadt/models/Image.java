/**
 * @author Sebastian Hamm
 * @filename Image.java
 * @created 6/23/25, Monday
 */

package at.sebastianhamm.kapelle_eisenstadt.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "app_images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "path")
    private String path;

    @Column(nullable = false, name = "category")
    private String category;

    @Column(nullable = false, name = "author")
    private String author;
}
