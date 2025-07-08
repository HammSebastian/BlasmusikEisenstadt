package at.sebastianhamm.backend.repository;


import java.util.Optional;

import at.sebastianhamm.backend.models.ERole;
import at.sebastianhamm.backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
