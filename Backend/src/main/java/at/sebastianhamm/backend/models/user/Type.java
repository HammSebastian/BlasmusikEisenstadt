package at.sebastianhamm.backend.models.user;

import at.sebastianhamm.backend.models.common.enums.EType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a general type or category, often used in conjunction
 * with announcements or other content to classify them.
 * Stores the type using the EType enum for consistency.
 */
@Entity
@Table(name = "types")
@Getter
@Setter
@NoArgsConstructor
public class Type {

    /**
     * Unique identifier for the type.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The actual type or category, stored as a string representation of the EType enum.
     * Must be unique and not null.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false, unique = true)
    private EType type;

    /**
     * Constructor for creating a Type entity with the specified EType.
     *
     * @param type The EType enum value for this type.
     */
    public Type(EType type) {
        this.type = type;
    }
}
