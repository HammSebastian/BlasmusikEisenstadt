package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.models.gig.enums.EGigs;
import at.sebastianhamm.backend.models.gig.GigType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for GigType entity.
 */
@Repository
public interface GigTypeRepository extends JpaRepository<GigType, Long> {

    /**
     * Finds GigType by EGigs enum value.
     *
     * @param egigs the EGigs enum
     * @return Optional of GigType
     */
    Optional<GigType> findByEgigs(EGigs egigs);

    /**
     * Checks if a GigType exists by EGigs enum value.
     *
     * @param egigs the EGigs enum
     * @return true if exists
     */
    boolean existsByEgigs(EGigs egigs);
}
