package at.sebastianhamm.backend.models.user;

import at.sebastianhamm.backend.models.common.enums.ERole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a security role within the application.
 * Stores the role name using the ERole enum for consistency
 * with Spring Security access control.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {

    /**
     * Unique identifier for the role.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The name of the role, stored as a string representation of the ERole enum.
     * Must be unique and not null.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false, unique = true)
    private ERole name; // Field name 'name' is good and descriptive.
}
