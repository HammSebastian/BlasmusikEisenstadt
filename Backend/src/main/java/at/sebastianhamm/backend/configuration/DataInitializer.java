package at.sebastianhamm.backend.configuration;

import at.sebastianhamm.backend.models.*;
import at.sebastianhamm.backend.models.Mission;
import at.sebastianhamm.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private final GigRepository gigRepository;
    private final GigTypeRepository gigTypeRepository;
    private final MemberRepository memberRepository;
    private final RemarkRepository remarkRepository;
    private final AboutRepository aboutRepository;
    private final MissionRepository missionRepository;


    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Override
    @Transactional
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
            Announcement announcement = new Announcement();
            announcement.setTitle("Stadtkapelle Eisenstadt");
            announcement.setMessage("Herzlich Willkommen in der Stadtkapelle Eisenstadt");
            Type announcementType = typeRepository.findByType(EType.ANNOUNCEMENT)
                    .orElseThrow(() -> new RuntimeException("EType.ANNOUNCEMENT fehlt"));

            announcement.setTypes(Set.of(announcementType));

            announcement.setStartDate(new java.util.Date());
            announcement.setEndDate(new java.util.Date());
            announcement.setCreatedBy("Admin");
            announcementsRepository.save(announcement);
            System.out.println("Default announcement created");
        }

        if (announcementsRepository.existsById(2L)) {
            System.out.println("Default announcement already exists");
        } else {
            Announcement announcement = new Announcement();
            announcement.setTitle("Stadtkapelle Eisenstadt");
            announcement.setMessage("Herzlich Willkommen in der Stadtkapelle Eisenstadt");
            Type announcementType = typeRepository.findByType(EType.INFO)
                    .orElseThrow(() -> new RuntimeException("EType.ANNOUNCEMENT fehlt"));

            announcement.setTypes(Set.of(announcementType));

            announcement.setStartDate(new java.util.Date());
            announcement.setEndDate(new java.util.Date());
            announcement.setCreatedBy("Admin");
            announcementsRepository.save(announcement);
            System.out.println("Default announcement created");
        }

        if (announcementsRepository.existsById(3L)) {
            System.out.println("Default announcement already exists");
        } else {
            Announcement announcement = new Announcement();
            announcement.setTitle("Stadtkapelle Eisenstadt");
            announcement.setMessage("Herzlich Willkommen in der Stadtkapelle Eisenstadt");
            Type announcementType = typeRepository.findByType(EType.MAINTENANCE)
                    .orElseThrow(() -> new RuntimeException("EType.ANNOUNCEMENT fehlt"));

            announcement.setTypes(Set.of(announcementType));

            announcement.setStartDate(new java.util.Date());
            announcement.setEndDate(new java.util.Date());
            announcement.setCreatedBy("Admin");
            announcementsRepository.save(announcement);
            System.out.println("Default announcement created");
        }

        // Initialize GigTypes if they don't exist
        for (EGigs egigs : EGigs.values()) {
            if (!gigTypeRepository.existsByEgigs(egigs)) {
                GigType gigType = new GigType(egigs);
                gigTypeRepository.save(gigType);
            }
        }

        if (gigRepository.existsById(1L)) {
            System.out.println("Default gig already exists");
        } else {
            Gig gig = new Gig();
            gig.setTitle("Stadtkapelle Eisenstadt");
            gig.setDescription("Herzlich Willkommen in der Stadtkapelle Eisenstadt");
            gig.setVenue("Stadtpark Eisenstadt");
            gig.setAddress("Hauptstraße 1, 7000 Eisenstadt");
            gig.setImageUrl("assets/images/HeroImage.png");
            gig.setNote("Bitte pünktlich erscheinen");
            gig.setDate(new java.util.Date());
            gig.setTime(java.sql.Time.valueOf("19:00:00"));
            gig.setCreatedBy("Admin");

            GigType gigType = gigTypeRepository.findByEgigs(EGigs.PERFORMANCE)
                    .orElseThrow(() -> new RuntimeException("EGigs.PERFORMANCE fehlt"));
            // Use the helper method from GigType to maintain the bidirectional relationship
            gigType.addGig(gig);

            gigRepository.save(gig);
            System.out.println("Default gig created");
        }

        for (RemarkType remarkType : RemarkType.values()) {
            if (!remarkRepository.existsByType(remarkType)) {
                Remark remark = new Remark(remarkType);
                remarkRepository.save(remark);
            }
        }

        if (memberRepository.existsById(1L)) {
            System.out.println("Default member already exists");
        } else {
            Member member = new Member();
            member.setName("Sebastian Hamm");
            member.setInstrument("Schlagzeug");
            member.setSection("Schlagwerk");
            member.setJoinDate("2023-01-01");
            member.setAvatarUrl("assets/images/Avatar.png");
            Remark remark = remarkRepository.findByType(RemarkType.PRIMARY)
                    .orElseThrow(() -> new RuntimeException("RemarkType.PRIMARY not found"));
            remark.setMember(member);
            member.addRemark(remark);
            memberRepository.save(member);
            System.out.println("Default member created");
        }

        About about = aboutRepository.findById(1L).orElseGet(() -> {
            About newAbout = new About();
            newAbout.setStory("Unsere Geschichte beginnt mit einer Leidenschaft für Musik und Gemeinschaft...");
            newAbout.setImageUrl("assets/images/About.png");
            return aboutRepository.save(newAbout);
        });

// Vorhandene Missions löschen
        missionRepository.deleteByAbout(about);
        about.getMissions().clear();

// Missions anlegen und hinzufügen
        Mission mission1 = new Mission();
        mission1.setTitle("Gemeinschaft fördern");
        mission1.setDescription("Wir fördern den Zusammenhalt und die Gemeinschaft durch Musik.");
        mission1.setImageUrl("assets/images/About.png");

        Mission mission2 = new Mission();
        mission2.setTitle("Musikalische Exzellenz");
        mission2.setDescription("Wir streben nach musikalischer Qualität und kontinuierlicher Weiterentwicklung.");
        mission2.setImageUrl("assets/images/About.png");

// Setze die Beziehung nur über addMission, damit beide Seiten stimmen
        about.addMission(mission1);
        about.addMission(mission2);

        System.out.println("Mission count before save: " + about.getMissions().size());
        about.getMissions().forEach(m -> System.out.println(m.getTitle()));

// Speichern
        aboutRepository.save(about);

        System.out.println("About und Missionen initialisiert");
    }
}