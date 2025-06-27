/**
 * @author Sebastian Hamm
 * @filename Gig.java
 * @created 6/23/25, Monday
 */

package at.sebastianhamm.kapelle_eisenstadt.models;


import java.util.Date;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "app_gig")
public class Gig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "title")
    private String title;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "location")
    private String location;

    @Column(nullable = false, name = "date")
    private Date date;

    @Column(nullable = false, name = "image")
    private String image;
}
