/**
 * @author Sebastian Hamm
 * @filename Gig.java
 * @created 6/23/25, Monday
 */

package at.sebastianhamm.kapelle_eisenstadt.entity;


import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Gig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "gig_title")
    private String gigTitel;

    @Column(nullable = false, name = "gig_description")
    private String gigDescription;

    @Column(nullable = false, name = "gig_location")
    private String gigLocation;

    @Column(nullable = false, name = "gig_date")
    private LocalDate gigDate;
}
