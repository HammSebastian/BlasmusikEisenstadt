package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.entity.Gig;
import at.sebastianhamm.backend.entity.Location;
import at.sebastianhamm.backend.entity.User;
import at.sebastianhamm.backend.enums.Type;
import at.sebastianhamm.backend.payload.request.GigRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GigRepository extends JpaRepository<Gig, Long> {

    // 🔹 Basisfilter
    List<Gig> findByUser(User user);
    List<Gig> findByUserId(Long userId);
    List<Gig> findByLocation(Location location);
    List<Gig> findByLocationId(Long locationId);

    // 🔹 Datumsspezifisch
    List<Gig> findByDate(LocalDate date); // exakt an einem Tag
    List<Gig> findByDateAfter(LocalDate date); // zukünftige Gigs
    List<Gig> findByDateBefore(LocalDate date); // vergangene Gigs
    List<Gig> findByDateBetween(LocalDate start, LocalDate end); // Zeitraum

    // 🔹 Kombinationen
    List<Gig> findByDateAndUser(LocalDate date, User user);
    List<Gig> findByLocationAndUser(Location location, User user);
    List<Gig> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
    List<Gig> findByUserIdAndLocationId(Long userId, Long locationId);

    // 🔹 Suche nach Titel (für Freitextfilter)
    List<Gig> findByTitleContainingIgnoreCase(String titlePart);
    List<Gig> findByUserIdAndTitleContainingIgnoreCase(Long userId, String titlePart);

    // 🔹 Suche nach Typ
    List<Gig> findByType(Type type); // funktioniert nur, wenn Type als String-Enum gespeichert ist
    List<Gig> findByUser_IdAndType(Long userId, Type type);

    // 🔹 Einzelelemente
    Optional<Gig> findByIdAndUserId(Long id, Long userId);

    // 🔹 Existenzprüfung
    boolean existsByUserIdAndDateAndTitle(Long userId, LocalDate date, String title);
}
