package at.sebastianhamm.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a type/category.
 * Stores type as Enum string for consistency.
 */
@Entity
@Table(name = "types")
@Getter
@Setter
@NoArgsConstructor
public class Type {

    /**
     * Primary key identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Enum representing the type/category.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false, unique = true)
    private EType type;

    /**
     * Constructor with EType.
     *
     * @param type Enum type value
     */
    public Type(EType type) {
        this.type = type;
    }
}
