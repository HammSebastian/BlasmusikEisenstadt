package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.entity.Rehearsal;
import at.sebastianhamm.backend.entity.User;
import at.sebastianhamm.backend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RehearsalRepository extends JpaRepository<Rehearsal, Long> {
    List<Rehearsal> findByUser(User user);
    List<Rehearsal> findByLocation(Location location);
    List<Rehearsal> findByDate(LocalDate date);
    List<Rehearsal> findByTitleContainingIgnoreCase(String title);

    Rehearsal getById(Long id);
}
