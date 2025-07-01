package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.entity.GigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GigRepository extends JpaRepository<GigEntity, Long> {

    Optional<GigEntity> findGigEntityByTitle(String title);

    boolean existsByTitle(String title);
    
    @Query("SELECT COUNT(g) > 0 FROM GigEntity g WHERE LOWER(g.title) = LOWER(:title)")
    boolean existsByTitleIgnoreCase(@Param("title") String title);

    List<GigEntity> findGigEntitiesByLocation(String location);

    List<GigEntity> findGigEntitiesByDate(String date);
}
