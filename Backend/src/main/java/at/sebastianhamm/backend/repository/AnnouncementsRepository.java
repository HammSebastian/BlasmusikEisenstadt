package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.models.Announcements;
import at.sebastianhamm.backend.models.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AnnouncementsRepository extends JpaRepository<Announcements, Long> {

    List<Announcements> findAll();

    @Query("SELECT a FROM Announcements a LEFT JOIN FETCH a.types")
    List<Announcements> findAllWithTypes();

    Optional<Announcements> findById(Long id);

    boolean existsById(Long id);

    List<Announcements> findByTypes(Set<Type> types);
}
