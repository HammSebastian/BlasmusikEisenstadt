package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.models.announcement.HeroItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeroItemRepository extends JpaRepository<HeroItem, Long> {

    HeroItem findHeroItemById(Long id);

}
