/**
 * @author Sebastian Hamm
 * @filename UserDao.java
 * @created 6/25/25, Wednesday
 */

package at.sebastianhamm.kapelle_eisenstadt.dao;

import at.sebastianhamm.kapelle_eisenstadt.models.Role;
import at.sebastianhamm.kapelle_eisenstadt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);

    List<Role> findUsersByRole(Role role);
}
