package at.sebastianhamm.backend.init;

import at.sebastianhamm.backend.entity.*;
import at.sebastianhamm.backend.enums.ParticipationStatus;
import at.sebastianhamm.backend.enums.Role;
import at.sebastianhamm.backend.enums.Type;
import at.sebastianhamm.backend.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final GigRepository gigRepository;
    private final PasswordEncoder passwordEncoder;
    private final RehearsalRepository rehearsalRepository;
    private final ParticipationRepository participationRepository;

    @PostConstruct
    public void init() {
        // Admin User anlegen
        Optional<User> adminOpt = userRepository.findByEmail("admin@stadtkapelle-eisenstadt.at");
        User admin;

        if (adminOpt.isEmpty()) {
            admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@stadtkapelle-eisenstadt.at");
            admin.setPassword(passwordEncoder.encode("Stadtkapelle1!")); // unbedingt später ändern
            admin.setRole(Role.ADMIN);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            admin = userRepository.save(admin);
        } else {
            admin = adminOpt.get();
        }

        // Locations prüfen und ggf. erstellen
        Location eisenstadt = locationRepository.findByCity("Eisenstadt").stream().findFirst().orElse(null);
        Location wien = locationRepository.findByCity("Wien").stream().findFirst().orElse(null);

        if (eisenstadt == null || wien == null) {
            eisenstadt = Location.builder()
                    .city("Eisenstadt")
                    .country("Austria")
                    .zipCode("7000")
                    .street("Hauptstraße")
                    .houseNumber("1")
                    .user(admin)
                    .build();

            wien = Location.builder()
                    .city("Wien")
                    .country("Austria")
                    .zipCode("1010")
                    .street("Ringstraße")
                    .houseNumber("10")
                    .user(admin)
                    .build();

            locationRepository.saveAll(List.of(eisenstadt, wien));
        }

        // Gigs
        if (gigRepository.count() == 0) {
            Gig gig1 = Gig.builder()
                    .title("Sommerkonzert")
                    .description("Sommerliches Konzert der Stadtkapelle")
                    .type(Type.CONCERT)
                    .date(LocalDate.now().plusDays(10))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .location(eisenstadt)
                    .user(admin)
                    .build();

            Gig gig2 = Gig.builder()
                    .title("Neujahrskonzert")
                    .description("Festliches Neujahrskonzert in Wien")
                    .type(Type.CONCERT)
                    .date(LocalDate.now().plusMonths(1))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .location(wien)
                    .user(admin)
                    .build();

            gigRepository.saveAll(List.of(gig1, gig2));
        }

        // Rehearsals
        if (rehearsalRepository.count() == 0) {
            Rehearsal rehearsal1 = Rehearsal.builder()
                    .title("Frühjahrsprobe")
                    .description("Probe für das Frühjahrskonzert")
                    .date(LocalDate.of(2025, 4, 10))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .user(admin)
                    .location(eisenstadt)
                    .build();

            Rehearsal rehearsal2 = Rehearsal.builder()
                    .title("Generalprobe")
                    .description("Generalprobe für das Sommerkonzert")
                    .date(LocalDate.of(2025, 7, 19))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .user(admin)
                    .location(wien)
                    .build();

            rehearsalRepository.saveAll(List.of(rehearsal1, rehearsal2));

            // Participations direkt anlegen (Beispieldaten)
            Participation p1 = Participation.builder()
                    .user(admin)
                    .rehearsal(rehearsal1)
                    .status(ParticipationStatus.YES)
                    .respondedAt(LocalDateTime.now())
                    .build();

            Participation p2 = Participation.builder()
                    .user(admin)
                    .rehearsal(rehearsal2)
                    .status(ParticipationStatus.NO)
                    .reason("Bin verhindert")
                    .respondedAt(LocalDateTime.now())
                    .build();

            participationRepository.saveAll(List.of(p1, p2));
        }
    }
}
