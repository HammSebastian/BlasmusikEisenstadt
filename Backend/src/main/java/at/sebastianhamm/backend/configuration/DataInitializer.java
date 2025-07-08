package at.sebastianhamm.backend.configuration;

import at.sebastianhamm.backend.models.*;
import at.sebastianhamm.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HeroItemRepository heroItemRepository;
    private final AnnouncementsRepository announcementsRepository;
    private final TypeRepository typeRepository;


    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        // Rollen anlegen, falls nicht da
        for (ERole erole : ERole.values()) {
            if (roleRepository.findByName(erole).isEmpty()) {
                Role role = new Role();
                role.setName(erole);
                roleRepository.save(role);
                System.out.println("Rolle hinzugefügt: " + erole.name());
            }
        }

        if (userRepository.findByUsername("Admin").isPresent()) {
            System.out.println("Admin user already exists");
        } else {
            User newUser = new User();
            newUser.setUsername("Admin");
            newUser.setEmail("admin@stadtkapelle-eisenstadt.at");
            newUser.setPassword(passwordEncoder.encode(adminPassword));

            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            newUser.setRoles(roles);

            userRepository.save(newUser);
            System.out.println("Admin user hinzugefügt");
        }

        if (heroItemRepository.existsById(1L)) {
            System.out.println("HeroItem already exists");
        } else {
            HeroItem heroItem = new HeroItem();
            heroItem.setTitle("Stadtkapelle Eisenstadt");
            heroItem.setDescription("Herzlich Willkommen in der Stadtkapelle Eisenstadt");
            heroItem.setImageUrl("assets/images/HeroImage.png");
            heroItemRepository.save(heroItem);
            System.out.println("HeroItem hinzugefügt");
        }

        // Types anlegen, falls nicht vorhanden
        for (EType etype : EType.values()) {
            if (typeRepository.findByType(etype).isEmpty()) {
                typeRepository.save(new Type(etype));
                System.out.println("Type hinzugefügt: " + etype.name());
            }
        }



        // Create a default Announcement
        if (announcementsRepository.existsById(1L)) {
            System.out.println("Default announcement already exists");
        } else {
            Announcements announcements = new Announcements();
            announcements.setTitle("Stadtkapelle Eisenstadt");
            announcements.setMessage("Herzlich Willkommen in der Stadtkapelle Eisenstadt");
            Type announcementType = typeRepository.findByType(EType.ANNOUNCEMENT)
                    .orElseThrow(() -> new RuntimeException("EType.ANNOUNCEMENT fehlt"));

            announcements.setTypes(Set.of(announcementType));

            announcements.setStartDate(new java.util.Date());
            announcements.setEndDate(new java.util.Date());
            announcements.setCreatedBy("Admin");
            announcementsRepository.save(announcements);
            System.out.println("Default announcement created");
        }

        if (announcementsRepository.existsById(2L)) {
            System.out.println("Default announcement already exists");
        } else {
            Announcements announcements = new Announcements();
            announcements.setTitle("Stadtkapelle Eisenstadt");
            announcements.setMessage("Herzlich Willkommen in der Stadtkapelle Eisenstadt");
            Type announcementType = typeRepository.findByType(EType.INFO)
                    .orElseThrow(() -> new RuntimeException("EType.ANNOUNCEMENT fehlt"));

            announcements.setTypes(Set.of(announcementType));

            announcements.setStartDate(new java.util.Date());
            announcements.setEndDate(new java.util.Date());
            announcements.setCreatedBy("Admin");
            announcementsRepository.save(announcements);
            System.out.println("Default announcement created");
        }

        if (announcementsRepository.existsById(3L)) {
            System.out.println("Default announcement already exists");
        } else {
            Announcements announcements = new Announcements();
            announcements.setTitle("Stadtkapelle Eisenstadt");
            announcements.setMessage("Herzlich Willkommen in der Stadtkapelle Eisenstadt");
            Type announcementType = typeRepository.findByType(EType.MAINTENANCE)
                    .orElseThrow(() -> new RuntimeException("EType.ANNOUNCEMENT fehlt"));

            announcements.setTypes(Set.of(announcementType));

            announcements.setStartDate(new java.util.Date());
            announcements.setEndDate(new java.util.Date());
            announcements.setCreatedBy("Admin");
            announcementsRepository.save(announcements);
            System.out.println("Default announcement created");
        }
    }
}