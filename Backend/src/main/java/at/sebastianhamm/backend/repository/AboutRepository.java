package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.models.announcement.About;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AboutRepository extends JpaRepository<About, Long> {

    Optional<About> findById(Long id);

    boolean existsById(Long id);

    @Query("SELECT a FROM About a LEFT JOIN FETCH a.missions WHERE a.id = :id")
    Optional<About> findByIdWithMissions(@Param("id") Long id);

}
