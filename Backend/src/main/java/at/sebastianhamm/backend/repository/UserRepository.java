package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.entity.User;
import at.sebastianhamm.backend.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔹 Authentifizierung / Login
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);

    // 🔹 Suche (z.B. Userverwaltung im Adminbereich)
    List<User> findByUsernameContainingIgnoreCase(String usernamePart);
    List<User> findByEmailContainingIgnoreCase(String emailPart);

    // 🔹 Rollenbasiert (z.B. nur ADMINs anzeigen)
    List<User> findByRole(Role role); // vorausgesetzt Role ist Enum als String gespeichert
    List<User> findByRoleOrderByUsernameAsc(Role role);

    // 🔹 Existenzprüfungen (z. B. bei Registrierung oder Update)
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameOrEmail(String username, String email);

    // 🔹 Zugriffsschutz (für z.B. Passwortänderung nur am eigenen Account)
    Optional<User> findByIdAndUsername(Long id, String username);
}