package at.sebastianhamm.backend.repository;

import aj.org.objectweb.asm.commons.Remapper;
import at.sebastianhamm.backend.entity.GigEntity;
import at.sebastianhamm.backend.io.GigResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GigRepository extends JpaRepository<GigEntity, Long> {

    Optional<GigEntity> findByTitleIgnoreCase(String title);

    boolean existsByTitleIgnoreCase(String title);

    List<GigEntity> findByLocation(String location);

    List<GigEntity> findByDate(LocalDate date);

    List<GigEntity> findGigEntitiesByDate(LocalDate date);

    List<GigEntity> findGigEntitiesByDateBetween(LocalDate startDate, LocalDate endDate);

    List<GigEntity> findGigEntitiesByLocation(String location);

    GigResponse findGigEntityById(Long id);
}
