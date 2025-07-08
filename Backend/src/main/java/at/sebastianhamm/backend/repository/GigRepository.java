package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.models.Gig;
import at.sebastianhamm.backend.models.GigType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for Gig entity.
 * Eagerly loads gigTypes to prevent N+1 issues.
 */
@Repository
public interface GigRepository extends JpaRepository<Gig, Long> {

    /**
     * Finds all gigs with gigTypes eagerly fetched.
     *
     * @return list of gigs with gigTypes
     */
    @EntityGraph(attributePaths = "gigTypes")
    List<Gig> findAll();

    /**
     * Finds gig by ID.
     *
     * @param id gig ID
     * @return Optional gig
     */
    Optional<Gig> findById(Long id);

    /**
     * Checks existence of gig by ID.
     *
     * @param id gig ID
     * @return true if exists
     */
    boolean existsById(Long id);

    /**
     * Finds gigs filtered by gigTypes.
     *
     * @param gigTypes set of gig types
     * @return list of gigs
     */
    List<Gig> findByGigTypesIn(Set<GigType> gigTypes);
}
