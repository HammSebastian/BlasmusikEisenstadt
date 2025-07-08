package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User by username.
     *
     * @param username the username
     * @return Optional User
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists by username.
     *
     * @param username the username
     * @return true if exists
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a user exists by email.
     *
     * @param email the email
     * @return true if exists
     */
    Boolean existsByEmail(String email);
}
