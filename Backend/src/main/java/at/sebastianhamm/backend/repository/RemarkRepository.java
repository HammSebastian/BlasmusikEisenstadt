package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.models.remark.Remark;
import at.sebastianhamm.backend.models.remark.RemarkType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RemarkRepository extends JpaRepository<Remark, Long> {

    Optional<Remark> findByType(RemarkType type);

    boolean existsByType(RemarkType type);
}
