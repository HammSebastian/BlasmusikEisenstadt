package at.sebastianhamm.backend.configuration;


import at.sebastianhamm.backend.models.announcement.About;
import at.sebastianhamm.backend.models.announcement.Announcement;
import at.sebastianhamm.backend.models.announcement.HeroItem;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class DataInitializerTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private HeroItemRepository heroItemRepository;
    @Mock
    private AnnouncementsRepository announcementsRepository;
    @Mock
    private TypeRepository typeRepository;
    @Mock
    private GigRepository gigRepository;
    @Mock
    private GigTypeRepository gigTypeRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RemarkRepository remarkRepository;
    @Mock
    private AboutRepository aboutRepository;
    @Mock
    private MissionRepository missionRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(dataInitializer, "adminPassword", "TestPass123");

        // Rollen-Mocks: Immer eine Rolle zurückgeben
        for (ERole role : ERole.values()) {
            when(roleRepository.findByName(role)).thenReturn(Optional.of(new Role()));
        }

        // Admin-User nicht vorhanden, damit addAdminUser() save aufruft
        when(userRepository.findByUsername("Admin")).thenReturn(Optional.empty());

        // Passwort wird kodiert
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // HeroItem noch nicht vorhanden
        when(heroItemRepository.existsById(1L)).thenReturn(false);

        // Alle Typen vorhanden (damit addAnnouncements() nicht fehlschlägt)
        for (EType type : EType.values()) {
            when(typeRepository.findByType(type)).thenReturn(Optional.of(new Type(type)));
        }

        // Ankündigungen noch nicht vorhanden
        when(announcementsRepository.existsById(anyLong())).thenReturn(false);

        // GigType noch nicht vorhanden (für addEGigs)
        when(gigTypeRepository.existsByEgigs(any())).thenReturn(false);
        for (EGigs egig : EGigs.values()) {
            when(gigTypeRepository.findByEgigs(egig)).thenReturn(Optional.of(new GigType()));
        }

        // Gigs noch nicht vorhanden
        when(gigRepository.existsById(anyLong())).thenReturn(false);

        // Member noch nicht vorhanden
        when(memberRepository.existsById(1L)).thenReturn(false);

        // RemarkType noch nicht vorhanden
        when(remarkRepository.existsByType(any())).thenReturn(false);
        when(remarkRepository.findByType(RemarkType.PRIMARY)).thenReturn(Optional.of(new Remark()));

        // About-Repository Save soll Objekt zurückgeben
        when(aboutRepository.save(any(About.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Mock role repository to return empty optional for all role lookups
        // This will trigger the save operation in addRoles()
        when(roleRepository.findByName(any(ERole.class))).thenReturn(Optional.empty());
        
        // Mock the admin role to be found when addAdminUser() looks for it
        Role adminRole = new Role();
        adminRole.setName(ERole.ROLE_ADMIN);
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
        
        // Mock user lookup to return empty (user doesn't exist yet)
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        
        // Create test types
        Type infoType = new Type();
        infoType.setType(EType.INFO);
        Type warningType = new Type();
        warningType.setType(EType.WARNING);
        Type maintenanceType = new Type();
        maintenanceType.setType(EType.MAINTENANCE);
        Type announcementType = new Type();
        announcementType.setType(EType.ANNOUNCEMENT);
        
        // Mock type repository to handle type lookups
        when(typeRepository.findByType(any(EType.class))).thenAnswer(invocation -> {
            EType type = invocation.getArgument(0);
            // For addAnnouncements()
            if (type == EType.ANNOUNCEMENT) return Optional.of(announcementType);
            if (type == EType.INFO) return Optional.of(infoType);
            if (type == EType.MAINTENANCE) return Optional.of(maintenanceType);
            // For addTypes() - return empty to trigger save
            return Optional.empty();
        });
        
        // Mock type repository save to return the type
        when(typeRepository.save(any(Type.class))).thenAnswer(invocation -> {
            Type type = invocation.getArgument(0);
            // Return the appropriate mock type based on the type being saved
            switch (type.getType()) {
                case INFO: return infoType;
                case WARNING: return warningType;
                case MAINTENANCE: return maintenanceType;
                case ANNOUNCEMENT: return announcementType;
                default: return type;
            }
        });
        
        // Mock announcements to not exist yet
        when(announcementsRepository.existsById(anyLong())).thenReturn(false);
        
        // Mock announcements save to return the announcement
        when(announcementsRepository.save(any(Announcement.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void run_AllAddMethodsCalled() throws Exception {
        dataInitializer.run();

        verify(roleRepository, atLeastOnce()).save(any(Role.class));
        verify(userRepository).save(any(User.class)); // Wichtig: Admin-User wird gespeichert
        verify(heroItemRepository).save(any(HeroItem.class));
        verify(typeRepository, atLeastOnce()).save(any(Type.class));
        verify(announcementsRepository, atLeastOnce()).save(any(Announcement.class));
        verify(gigTypeRepository, atLeastOnce()).save(any(GigType.class));
        verify(gigRepository, atLeastOnce()).save(any(Gig.class));
        verify(memberRepository).save(any(Member.class));
        verify(remarkRepository, atLeastOnce()).save(any(Remark.class));
        verify(aboutRepository, times(2)).save(any(About.class));
        verify(missionRepository).deleteByAbout(any(About.class));
    }


    @Test
    void addAdminUser_UserExists_NoSave() {
        when(userRepository.findByUsername("Admin")).thenReturn(Optional.of(new User()));

        dataInitializer.addAdminUser();

        verify(userRepository, never()).save(any());
    }

    @Test
    void addAdminUser_RoleMissing_NoSave() {
        when(userRepository.findByUsername("Admin")).thenReturn(Optional.empty());
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());

        dataInitializer.addAdminUser();

        verify(userRepository, never()).save(any());
    }

    @Test
    void addAdminUser_ValidData_SaveCalled() {
        when(userRepository.findByUsername("Admin")).thenReturn(Optional.empty());
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(new Role()));
        when(passwordEncoder.encode(any())).thenReturn("encodedPass");

        dataInitializer.addAdminUser();

        verify(userRepository).save(any(User.class));
    }

    @Test
    void addRoles_RolesNotPresent_SavesRoles() {
        when(roleRepository.findByName(any())).thenReturn(Optional.empty());

        dataInitializer.addRoles();

        verify(roleRepository, atLeast(ERole.values().length)).save(any(Role.class));
    }

    @Test
    void addRoles_RolesAlreadyExist_NoSave() {
        when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role()));

        dataInitializer.addRoles();

        verify(roleRepository, never()).save(any());
    }

    @Test
    void addHeroItem_NotExists_SavesHeroItem() {
        when(heroItemRepository.existsById(1L)).thenReturn(false);

        dataInitializer.addHeroItem();

        verify(heroItemRepository).save(any(HeroItem.class));
    }

    @Test
    void addHeroItem_Exists_NoSave() {
        when(heroItemRepository.existsById(1L)).thenReturn(true);

        dataInitializer.addHeroItem();

        verify(heroItemRepository, never()).save(any());
    }

    @Test
    void addTypes_AllTypesMissing_SavesAll() {
        when(typeRepository.findByType(any())).thenReturn(Optional.empty());

        dataInitializer.addTypes();

        verify(typeRepository, atLeast(EType.values().length)).save(any(Type.class));
    }

    @Test
    void addTypes_AllTypesPresent_NoSave() {
        when(typeRepository.findByType(any())).thenReturn(Optional.of(new Type()));

        dataInitializer.addTypes();

        verify(typeRepository, never()).save(any());
    }

    @Test
    void addAnnouncements_AllMissing_SavesAll() {
        when(announcementsRepository.existsById(anyLong())).thenReturn(false);
        when(typeRepository.findByType(any())).thenReturn(Optional.of(new Type()));

        dataInitializer.addAnnouncements();

        verify(announcementsRepository, atLeast(3)).save(any(Announcement.class));
    }

    @Test
    void addAnnouncements_Exists_NoSave() {
        when(announcementsRepository.existsById(anyLong())).thenReturn(true);

        dataInitializer.addAnnouncements();

        verify(announcementsRepository, never()).save(any());
    }

    @Test
    void addEGigs_AllMissing_SavesAll() {
        when(gigTypeRepository.existsByEgigs(any())).thenReturn(false);

        dataInitializer.addEGigs();

        verify(gigTypeRepository, atLeast(EGigs.values().length)).save(any(GigType.class));
    }

    @Test
    void addEGigs_AllPresent_NoSave() {
        when(gigTypeRepository.existsByEgigs(any())).thenReturn(true);

        dataInitializer.addEGigs();

        verify(gigTypeRepository, never()).save(any());
    }

    @Test
    void addGigs_AllMissing_SavesAll() {
        when(gigRepository.existsById(anyLong())).thenReturn(false);
        when(gigTypeRepository.findByEgigs(EGigs.PERFORMANCE)).thenReturn(Optional.of(new GigType()));

        dataInitializer.addGigs();

        verify(gigRepository, atLeast(3)).save(any(Gig.class));
    }

    @Test
    void addGigs_Exists_NoSave() {
        when(gigRepository.existsById(anyLong())).thenReturn(true);

        dataInitializer.addGigs();

        verify(gigRepository, never()).save(any());
    }

    @Test
    void addMember_NotExists_SavesMember() {
        when(memberRepository.existsById(1L)).thenReturn(false);
        when(remarkRepository.findByType(RemarkType.PRIMARY)).thenReturn(Optional.of(new Remark()));

        dataInitializer.addMember();

        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void addMember_Exists_NoSave() {
        when(memberRepository.existsById(1L)).thenReturn(true);

        dataInitializer.addMember();

        verify(memberRepository, never()).save(any());
    }

    @Test
    void addRemarks_NoneExist_SavesAll() {
        when(remarkRepository.existsByType(any())).thenReturn(false);

        dataInitializer.addRemarks();

        verify(remarkRepository, atLeast(RemarkType.values().length)).save(any(Remark.class));
    }

    @Test
    void addRemarks_AllExist_NoSave() {
        when(remarkRepository.existsByType(any())).thenReturn(true);

        dataInitializer.addRemarks();

        verify(remarkRepository, never()).save(any());
    }
}
