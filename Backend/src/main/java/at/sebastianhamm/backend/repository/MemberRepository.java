package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.models.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "remarks")
    List<Member> findAll();

    @EntityGraph(attributePaths = "remarks")
    Optional<Member> findById(Long id);

    boolean existsById(Long id);

    List<Member> findByName(String name);

    List<Member> findBySection(String section);

    List<Member> findMemberByInstrument(String instrument);
}
