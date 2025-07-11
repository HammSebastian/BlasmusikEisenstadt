package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.entity.Gig;
import at.sebastianhamm.backend.entity.Participation;
import at.sebastianhamm.backend.entity.Rehearsal;
import at.sebastianhamm.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findAllByRehearsal(Rehearsal rehearsal);
    List<Participation> findAllByGig(Gig gig);

    Optional<Participation> findByUserAndGig(User user, Gig gig);
    Optional<Participation> findByUserAndRehearsal(User user, Rehearsal rehearsal);
}
