package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.models.gig.Gig;
import at.sebastianhamm.backend.models.gig.GigType;
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
     * Overrides the default findAll() to use an EntityGraph.
     *
     * @return list of gigs with gigTypes
     */
    @Override // It's good practice to mark overridden methods with @Override
    @EntityGraph(attributePaths = "gigTypes")
    List<Gig> findAll();

    /**
     * Finds gig by ID with gigTypes eagerly fetched.
     * This method will use an EntityGraph to load gigTypes when fetching a single Gig.
     *
     * @param id gig ID
     * @return Optional gig with gigTypes
     */
    @EntityGraph(attributePaths = "gigTypes")
    Optional<Gig> findById(Long id); // <-- This is the change!

    /**
     * Checks existence of gig by ID.
     *
     * @param id gig ID
     * @return true if exists
     */
    boolean existsById(Long id);

    /**
     * Finds gigs filtered by gigTypes.
     * Note: This method might still face N+1 if GigType also has lazy collections,
     * or if you need to access other lazy collections on Gig.
     * Consider adding @EntityGraph here if needed for gigTypes.
     *
     * @param gigTypes set of gig types
     * @return list of gigs
     */
    // If you need gigTypes to be loaded eagerly here as well for each Gig,
    // you would add @EntityGraph(attributePaths = "gigTypes") to this method too.
    List<Gig> findByGigTypesIn(Set<GigType> gigTypes);
}