package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.models.announcement.About;
import at.sebastianhamm.backend.models.announcement.Mission;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface MissionRepository extends Repository<Mission, Long> {
    
    Mission findByTitle(String title);

    Mission save(Mission mission);

    @Modifying
    @Transactional
    @Query("DELETE FROM Mission m WHERE m.about = :about")
    void deleteByAbout(About about);
}
