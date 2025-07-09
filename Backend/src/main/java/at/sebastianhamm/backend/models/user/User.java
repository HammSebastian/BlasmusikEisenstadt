package at.sebastianhamm.backend.models.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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
 * Entity representing a user in the application.
 * Includes user credentials (username, email, password) and associated roles.
 * Designed with validation constraints and unique keys for production readiness.
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
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username of the user. Must not be blank, unique, and between 1 and 20 characters.
     */
    @Column(nullable = false, length = 20, unique = true)
    @NotBlank(message = "Username must not be blank")
    @Size(max = 20, message = "Username must be at most 20 characters")
    private String username;

    /**
     * Email address of the user. Must not be blank, unique, valid email format, and max 50 characters.
     */
    @Column(nullable = false, length = 50, unique = true)
    @NotBlank(message = "Email must not be blank")
    @Size(max = 50, message = "Email must be at most 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * Hashed password of the user. Must not be blank and max 120 characters.
     * Passwords should always be stored as hashes (e.g., BCrypt).
     */
    @Column(nullable = false, length = 120)
    @NotBlank(message = "Password must not be blank")
    @Size(max = 120, message = "Password must be at most 120 characters")
    private String password;

    /**
     * Set of roles assigned to the user.
     * Represents a many-to-many relationship with Role entities.
     * Roles are lazily loaded.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * Constructor for creating a new User with essential authentication details.
     *
     * @param username The user's chosen username.
     * @param email    The user's email address.
     * @param password The user's hashed password.
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
