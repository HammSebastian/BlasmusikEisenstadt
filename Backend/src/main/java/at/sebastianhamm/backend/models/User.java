package at.sebastianhamm.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a User.
 * Hardened with validation, unique constraints, and clean design.
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Getter
@Setter
@NoArgsConstructor
public class User {

    /**
     * Primary key identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username of the user.
     * Not blank, max 20 characters.
     */
    @Column(nullable = false, length = 20, unique = true)
    @NotBlank(message = "Username must not be blank")
    @Size(max = 20, message = "Username must be at most 20 characters")
    private String username;

    /**
     * Email address.
     * Not blank, max 50 characters, valid email format.
     */
    @Column(nullable = false, length = 50, unique = true)
    @NotBlank(message = "Email must not be blank")
    @Size(max = 50, message = "Email must be at most 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * Hashed password.
     * Not blank, max 120 characters.
     * Password should be stored hashed and salted externally.
     */
    @Column(nullable = false, length = 120)
    @NotBlank(message = "Password must not be blank")
    @Size(max = 120, message = "Password must be at most 120 characters")
    private String password;

    /**
     * Roles assigned to the user.
     * Many-to-many relationship.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * Constructor with essential fields.
     *
     * @param username username
     * @param email email address
     * @param password hashed password
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
