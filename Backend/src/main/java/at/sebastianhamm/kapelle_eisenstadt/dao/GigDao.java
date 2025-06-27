/**
 * @author Sebastian Hamm
 * @filename GigDao.java
 * @created 6/25/25, Wednesday
 */

package at.sebastianhamm.kapelle_eisenstadt.dao;


import at.sebastianhamm.kapelle_eisenstadt.models.Gig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface GigDao extends JpaRepository<Gig, Long> {

    List<Gig> findGigsByTitle(String gigTitel);

    List<Gig> findGigsByLocation(String gigLocation);

    List<Gig> findGigsByDate(Date date);

    List<Gig> findGigsByDescription(String description);

    Optional<Gig> findGigByTitle(String gigTitel);
}
