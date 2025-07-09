package at.sebastianhamm.backend.configuration;

import at.sebastianhamm.backend.models.announcement.About;
import at.sebastianhamm.backend.models.announcement.Announcement;
import at.sebastianhamm.backend.models.announcement.HeroItem;
import at.sebastianhamm.backend.models.announcement.Mission;
import at.sebastianhamm.backend.models.common.enums.ERole;
import at.sebastianhamm.backend.models.common.enums.EType;
import at.sebastianhamm.backend.models.gig.Gig;
import at.sebastianhamm.backend.models.gig.GigType;
import at.sebastianhamm.backend.models.gig.enums.EGigs;
import at.sebastianhamm.backend.models.remark.Remark;
import at.sebastianhamm.backend.models.remark.RemarkType;
import at.sebastianhamm.backend.models.user.Member;
import at.sebastianhamm.backend.models.user.Role;
import at.sebastianhamm.backend.models.user.Type;
import at.sebastianhamm.backend.models.user.User;
import at.sebastianhamm.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Secure data initializer for application startup.
 * Ensures required baseline data is inserted into the database with validation,
 * error handling, atomic operations and logging.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    /**
     * Logger for initialization events and errors.
     */
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    /**
     * Default IDs for initial announcements.
     */
    private static final long[] DEFAULT_ANNOUNCEMENT_IDS = {1L, 2L, 3L};

    /**
     * Default IDs for initial gigs.
     */
    private static final long[] DEFAULT_GIG_IDS = {1L, 2L, 3L, 4L, 5L, 6L};

    /**
     * Default username for the administrator account.
     */
    private static final String DEFAULT_ADMIN_USERNAME = "Admin";

    /**
     * Default username for the reporter account.
     */
    private static final String DEFAULT_REPORTER_USERNAME = "Reporter";

    /**
     * Default username for the conductor account.
     */
    private static final String DEFAULT_CONDUCTOR_USERNAME = "Conductor";

    /**
     * Default username for the musician account.
     */
    private static final String DEFAULT_MUSICIAN_USERNAME = "Musician";

    /**
     * Minimum length required for the administrator password.
     */
    private static final int MIN_PASSWORD_LENGTH = 8;

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

    /**
     * Lock to prevent race conditions during initialization.
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Random number generator for date generation.
     */
    private final Random random = new Random();

    /**
     * Administrator password loaded from configuration.
     * Must meet minimum length requirements to allow initialization.
     */
    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Value("${REPORTER_PASSWORD:}")
    private String reporterPassword;

    @Value("${CONDUCTOR_PASSWORD:}")
    private String conductorPassword;

    @Value("${MUSICIAN_PASSWORD:}")
    private String musicianPassword;


    /**
     * Constructor for dependency injection e.g. for testing.
     *
     * @param roleRepository
     * @param userRepository
     * @param passwordEncoder
     * @param heroItemRepository
     * @param announcementsRepository
     * @param typeRepository
     * @param gigRepository
     * @param gigTypeRepository
     * @param memberRepository
     * @param remarkRepository
     * @param aboutRepository
     * @param missionRepository
     */
    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository,
                           PasswordEncoder passwordEncoder, HeroItemRepository heroItemRepository,
                           AnnouncementsRepository announcementsRepository, TypeRepository typeRepository,
                           GigRepository gigRepository, GigTypeRepository gigTypeRepository,
                           MemberRepository memberRepository, RemarkRepository remarkRepository,
                           AboutRepository aboutRepository, MissionRepository missionRepository
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.heroItemRepository = heroItemRepository;
        this.announcementsRepository = announcementsRepository;
        this.typeRepository = typeRepository;
        this.gigRepository = gigRepository;
        this.gigTypeRepository = gigTypeRepository;
        this.memberRepository = memberRepository;
        this.remarkRepository = remarkRepository;
        this.aboutRepository = aboutRepository;
        this.missionRepository = missionRepository;
    }

    /**
     * Main method triggered on application startup.
     * Executes all data initialization steps, validates inputs,
     * and aborts if critical data (e.g. admin password) is not valid.
     *
     * @param args runtime arguments (ignored)
     */
    @Override
    @Transactional
    public void run(String... args) {
        if (!validateAdminPassword(adminPassword)) {
            logger.error("Admin password does not meet security requirements (min {} chars). Initialization aborted.", MIN_PASSWORD_LENGTH);
            return; // abort initialization to avoid insecure default user
        }

        addRoles();
        addAdminUser();
        addReporterUser();
        addConductorUser();
        addMusicianUser();
        addHeroItem();
        addTypes();
        addAnnouncements();
        addEGigs();
        addGigs();
        addRemarks();
        addMember();
        addAbout();
    }

    /**
     * Validates the administrator password meets minimal security requirements.
     *
     * @param password the password to validate
     * @return true if valid, false otherwise
     */
    private boolean validateAdminPassword(String password) {
        return password != null && password.length() >= MIN_PASSWORD_LENGTH;
    }

    /**
     * Adds all predefined roles to the database if they do not exist.
     * Uses a lock to prevent race conditions.
     */
    protected void addRoles() {
        lock.lock();
        try {
            for (ERole erole : ERole.values()) {
                roleRepository.findByName(erole).ifPresentOrElse(
                        r -> logger.debug("Role {} already exists", erole),
                        () -> {
                            Role role = new Role();
                            role.setName(erole);
                            roleRepository.save(role);
                            logger.info("Role {} added", erole);
                        }
                );
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Creates the administrator user if it does not already exist.
     * Requires the admin role to exist; aborts with error log otherwise.
     * Uses lock to avoid concurrency issues.
     */
    protected void addAdminUser() {
        lock.lock();
        try {
            if (userRepository.findByUsername(DEFAULT_ADMIN_USERNAME).isPresent()) {
                logger.info("Admin user already exists");
                return;
            }

            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null);
            if (adminRole == null) {
                logger.error("Admin role missing. Cannot create admin user.");
                return;
            }

            User newUser = new User();
            newUser.setUsername(DEFAULT_ADMIN_USERNAME);
            newUser.setEmail("admin@stadtkapelle-eisenstadt.at");
            newUser.setPassword(passwordEncoder.encode(adminPassword));
            newUser.setRoles(Set.of(adminRole));

            userRepository.save(newUser);
            logger.info("Admin user created");
        } finally {
            lock.unlock();
        }
    }

    //reporter user, conductor user
    protected void addReporterUser() {
        lock.lock();
        try {
            if (userRepository.findByUsername(DEFAULT_REPORTER_USERNAME).isPresent()) {
                logger.info("Reporter user already exists");
                return;
            }

            Role reporterRole = roleRepository.findByName(ERole.ROLE_REPORTER).orElse(null);
            if (reporterRole == null) {
                logger.error("Reporter role missing. Cannot create reporter user.");
                return;
            }

            User newUser = new User();
            newUser.setUsername(DEFAULT_REPORTER_USERNAME);
            newUser.setEmail("reporter@stadtkapelle-eisenstadt.at");
            newUser.setPassword(passwordEncoder.encode(reporterPassword));
            newUser.setRoles(Set.of(reporterRole));

            userRepository.save(newUser);
            logger.info("Reporter user created");
        } finally {
            lock.unlock();
        }
    }

    protected void addConductorUser() {
        lock.lock();
        try {
            if (userRepository.findByUsername(DEFAULT_CONDUCTOR_USERNAME).isPresent()) {
                logger.info("Conductor user already exists");
                return;
            }

            Role conductorRole = roleRepository.findByName(ERole.ROLE_CONDUCTOR).orElse(null);
            if (conductorRole == null) {
                logger.error("Conductor role missing. Cannot create conductor user.");
                return;
            }

            User newUser = new User();
            newUser.setUsername(DEFAULT_CONDUCTOR_USERNAME);
            newUser.setEmail("conductor@stadtkapelle-eisenstadt.at");
            newUser.setPassword(passwordEncoder.encode(conductorPassword));
            newUser.setRoles(Set.of(conductorRole));

            userRepository.save(newUser);
            logger.info("Conductor user created");
        } finally {
            lock.unlock();
        }
    }

    protected void addMusicianUser() {
        lock.lock();
        try {
            if (userRepository.findByUsername(DEFAULT_MUSICIAN_USERNAME).isPresent()) {
                logger.info("Musician user already exists");
                return;
            }

            Role musicianRole = roleRepository.findByName(ERole.ROLE_MUSICIAN).orElse(null);
            if (musicianRole == null) {
                logger.error("Musician role missing. Cannot create musician user.");
                return;
            }

            User newUser = new User();
            newUser.setUsername(DEFAULT_MUSICIAN_USERNAME);
            newUser.setEmail("musician@stadtkapelle-eisenstadt.at");
            newUser.setPassword(passwordEncoder.encode(musicianPassword));
            newUser.setRoles(Set.of(musicianRole));

            userRepository.save(newUser);
            logger.info("Musician user created");
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adds a default HeroItem entry if it does not exist.
     */
    protected void addHeroItem() {
        if (heroItemRepository.existsById(1L)) {
            logger.info("HeroItem already exists");
            return;
        }
        HeroItem heroItem = new HeroItem();
        heroItem.setTitle("Stadtkapelle Eisenstadt");
        heroItem.setDescription("Herzlich Willkommen in der Stadtkapelle Eisenstadt");
        heroItem.setImageUrl("assets/images/HeroImage.png");

        heroItemRepository.save(heroItem);
        logger.info("HeroItem created");
    }

    /**
     * Adds all types defined in EType to the database if missing.
     */
    protected void addTypes() {
        for (EType etype : EType.values()) {
            if (typeRepository.findByType(etype).isEmpty()) {
                typeRepository.save(new Type(etype));
                logger.info("Type {} added", etype);
            }
        }
    }

    /**
     * Adds default announcements with fixed IDs and associated types.
     * Catches and logs errors if required types are missing.
     */
    protected void addAnnouncements() {
        for (int i = 0; i < DEFAULT_ANNOUNCEMENT_IDS.length; i++) {
            long id = DEFAULT_ANNOUNCEMENT_IDS[i];
            if (announcementsRepository.existsById(id)) {
                logger.info("Announcement with ID {} already exists", id);
                continue;
            }
            EType type = switch (i) {
                case 0 -> EType.ANNOUNCEMENT;
                case 1 -> EType.INFO;
                case 2 -> EType.MAINTENANCE;
                default -> EType.ANNOUNCEMENT;
            };
            try {
                Type announcementType = typeRepository.findByType(type)
                        .orElseThrow(() -> new IllegalStateException("Missing type: " + type));
                Announcement announcement = new Announcement();
                announcement.setTitle("Ankündigung " + id);
                announcement.setMessage("Nachricht von " + id + " zu " + type);
                announcement.setTypes(Set.of(announcementType));
                Date now = new Date();
                announcement.setStartDate(now);
                announcement.setEndDate(now);
                announcement.setCreatedBy(DEFAULT_ADMIN_USERNAME);

                announcementsRepository.save(announcement);
                logger.info("Announcement with ID {} created", id);
            } catch (IllegalStateException e) {
                logger.error("Failed to create announcement ID {}: {}", id, e.getMessage());
            }
        }
    }

    /**
     * Adds all gig types defined in EGigs to the database if missing.
     */
    protected void addEGigs() {
        for (EGigs egigs : EGigs.values()) {
            if (!gigTypeRepository.existsByEgigs(egigs)) {
                GigType gigType = new GigType(egigs);
                gigTypeRepository.save(gigType);
            }
        }
    }

    /**
     * Adds default gigs with fixed IDs and links them to the PERFORMANCE gig type.
     * Logs errors if gig type is missing.
     */
    protected void addGigs() {
        for (long id : DEFAULT_GIG_IDS) {
            if (gigRepository.existsById(id)) {
                logger.info("Gig with ID {} already exists", id);
                continue;
            }
            try {
                GigType gigType = gigTypeRepository.findByEgigs(EGigs.PERFORMANCE)
                        .orElseThrow(() -> new IllegalStateException("Missing GigType PERFORMANCE"));

                Gig gig = new Gig();
                gig.setTitle("Stadtkapelle Eisenstadt");
                gig.setDescription("Herzlich Willkommen in der Stadtkapelle Eisenstadt");
                gig.setVenue("Stadtpark Eisenstadt");
                gig.setAddress("Hauptstraße 1, 7000 Eisenstadt");
                gig.setImageUrl("assets/images/HeroImage.png");
                gig.setAdditionalInfo("Bitte pünktlich erscheinen");
                gig.setDate(generateRandomFutureDate(1, 5));
                gig.setTime(LocalTime.of(19, 0));

                gigType.addGig(gig);
                gigRepository.save(gig);
                logger.info("Gig with ID {} created", id);
            } catch (IllegalStateException e) {
                logger.error("Failed to create gig ID {}: {}", id, e.getMessage());
            }
        }
    }

    /**
     * Generates a random future date within a specified range.
     * @param minYears
     * @param maxYears
     * @return
     */
    private LocalDate generateRandomFutureDate(int minYears, int maxYears) {
        LocalDate today = LocalDate.now();
        long minDays = today.until(today.plusYears(minYears)).getDays();
        long maxDays = today.until(today.plusYears(maxYears)).getDays();

        if (maxDays <= minDays) {
            maxDays = minDays + 365;
        }

        long daysToAdd = minDays + random.nextInt((int) (maxDays - minDays + 1));

        return today.plusDays(daysToAdd);
    }

    /**
     * Adds the 'About' section and associated missions.
     * Deletes existing missions for the About entity before adding new ones.
     */
    private void addAbout() {
        if (aboutRepository.existsById(1L)) {
            logger.info("About with ID 1 already exists");
            return;
        }

        About about = aboutRepository.findById(1L).orElseGet(() -> {
            About newAbout = new About();
            newAbout.setStory("Unsere Geschichte beginnt mit einer Leidenschaft für Musik und Gemeinschaft...");
            newAbout.setImageUrl("assets/images/About.png");
            return aboutRepository.save(newAbout);
        });

        Mission mission1 = new Mission();
        mission1.setTitle("Gemeinschaft fördern");
        mission1.setDescription("Wir fördern den Zusammenhalt und die Gemeinschaft durch Musik.");
        mission1.setImageUrl("assets/images/About.png");

        Mission mission2 = new Mission();
        mission2.setTitle("Musikalische Exzellenz");
        mission2.setDescription("Wir streben nach musikalischer Qualität und kontinuierlicher Weiterentwicklung.");
        mission2.setImageUrl("assets/images/About.png");

        about.addMission(mission1);
        about.addMission(mission2);

        aboutRepository.save(about);
        logger.info("About with ID {} created", about.getId());
    }

    /**
     * Adds a default member if none exists.
     * Links the member to the primary remark type.
     * Logs error if required remark type is missing.
     */
    protected void addMember() {
        if (memberRepository.existsById(1L)) {
            logger.info("Member already exists");
            return;
        }
        try {
            Remark remark = remarkRepository.findByType(RemarkType.PRIMARY)
                    .orElseThrow(() -> new IllegalStateException("RemarkType.PRIMARY not found"));

            Member member = new Member();
            member.setName("Sebastian Hamm");
            member.setInstrument("Schlagzeug");
            member.setSection("Schlagwerk");
            member.setJoinDate("2023-01-01");
            member.setAvatarUrl("assets/images/Avatar.png");

            remark.setMember(member);
            member.addRemark(remark);

            memberRepository.save(member);
            logger.info("Member created");
        } catch (IllegalStateException e) {
            logger.error("Failed to create member: {}", e.getMessage());
        }
    }

    /**
     * Adds all remarks for each RemarkType if missing.
     */
    protected void addRemarks() {
        for (RemarkType remarkType : RemarkType.values()) {
            if (!remarkRepository.existsByType(remarkType)) {
                Remark remark = new Remark(remarkType);
                remarkRepository.save(remark);
            }
        }
    }
}
