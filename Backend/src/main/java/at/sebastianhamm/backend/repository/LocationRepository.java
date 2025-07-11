package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.entity.Location;
import at.sebastianhamm.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    // 🔹 Standard-Findings
    List<Location> findByUser(User user);
    List<Location> findByCity(String city);
    List<Location> findByCountry(String country);
    List<Location> findByCityAndCountry(String city, String country);
    List<Location> findByZipCode(String zipCode);
    List<Location> findByStreet(String street);
    List<Location> findByHouseNumber(String houseNumber);

    // 🔹 Teilwortsuche (für Autocomplete oder Freitext-Suche)
    List<Location> findByCityContainingIgnoreCase(String cityPart);
    List<Location> findByCountryContainingIgnoreCase(String countryPart);
    List<Location> findByStreetContainingIgnoreCase(String streetPart);
    List<Location> findByZipCodeContainingIgnoreCase(String zipPart);

    // 🔹 User-basierte Suche + Kombinationen
    List<Location> findByUserId(Long userId);
    List<Location> findByUserIdAndCityContainingIgnoreCase(Long userId, String city);

    List<Location> findByUserAndCountry(User user, String country);

    // 🔹 Gigs-bezogene Abfragen
    List<Location> findByGigsIsNotEmpty(); // Locations, die verwendet werden
    List<Location> findByGigsIsEmpty();    // Locations ohne Gigs (z. B. aufräumen)

    List<Location> findByGigsDateBetween(LocalDate start, LocalDate end); // Zeitraumfilter
    List<Location> findByGigsDateAfter(LocalDate date);                   // Zukünftige Gigs
    List<Location> findByGigsDateBefore(LocalDate date);                  // Vergangene Gigs

    List<Location> findByGigsUserId(Long userId); // Locations, bei denen ein bestimmter User Auftritte hat

    // 🔹 Gig-basierter Zugriff
    Optional<Location> findByGigs_Id(Long gigId); // Hole Ort zu einem spezifischen Gig

    // 🔹 Existenzprüfung
    boolean existsByCityAndCountryAndStreetAndHouseNumber(
            String city, String country, String street, String houseNumber
    );

    // 🔹 Duplikatsprüfung für denselben User
    boolean existsByUserIdAndCityAndStreetAndHouseNumber(
            Long userId, String city, String street, String houseNumber
    );
}