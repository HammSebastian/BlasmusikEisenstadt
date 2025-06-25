/**
 * @author Sebastian Hamm
 * @filename GigDao.java
 * @created 6/25/25, Wednesday
 */

package at.sebastianhamm.kapelle_eisenstadt.dao;


import at.sebastianhamm.kapelle_eisenstadt.entity.Gig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface GigDao extends JpaRepository<Gig, Long> {

    Gig findGigByGigTitel(String gigTitel);

    String findGigByGigLocation(String gigLocation);

    Gig findGigByGigDate(LocalDate gigDate);

    String findGigByGigDescription(String gigDescription);
}
