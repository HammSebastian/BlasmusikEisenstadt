package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.models.ERole;
import at.sebastianhamm.backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Role entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a Role by its enum name.
     *
     * @param name the ERole enum
     * @return Optional containing Role if found
     */
    Optional<Role> findByName(ERole name);
}
