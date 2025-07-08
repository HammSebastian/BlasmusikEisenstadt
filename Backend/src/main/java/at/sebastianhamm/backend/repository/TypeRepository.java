package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.models.EType;
import at.sebastianhamm.backend.models.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeRepository extends JpaRepository<Type, Integer> {
    Optional<Type> findByType(EType type);
}

