package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.entity.Gig;
import at.sebastianhamm.backend.entity.Participation;
import at.sebastianhamm.backend.entity.Rehearsal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Optional<Participation> findParticipationByGig(Gig gig);
    Optional<Participation> findParticipationByRehearsal(Rehearsal rehearsal);
}
