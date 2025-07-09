package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.models.common.enums.EType;
import at.sebastianhamm.backend.models.user.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Type entity.
 */
@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {

    /**
     * Finds a Type by its EType enum.
     *
     * @param type the EType enum
     * @return Optional containing the Type if found
     */
    Optional<Type> findByType(EType type);
}
