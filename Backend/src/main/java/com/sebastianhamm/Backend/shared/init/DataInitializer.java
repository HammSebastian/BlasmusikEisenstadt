/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.shared.init;
import com.sebastianhamm.Backend.member.domain.repositories.MemberRepository;
import com.sebastianhamm.Backend.image.domain.repositories.ImageRepository;
import com.sebastianhamm.Backend.gallery.domain.repositories.GalleryRepository;
import com.sebastianhamm.Backend.welcome.domain.repositories.WelcomeRepository;
import com.sebastianhamm.Backend.about.domain.repositories.AboutRepository;
import com.sebastianhamm.Backend.event.domain.repositories.EventRepository;
import com.sebastianhamm.Backend.location.domain.repositories.LocationRepository;
import com.sebastianhamm.Backend.member.domain.entities.MemberEntity;
import com.sebastianhamm.Backend.image.domain.entities.ImageEntity;
import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;
import com.sebastianhamm.Backend.welcome.domain.entities.WelcomeEntity;
import com.sebastianhamm.Backend.about.domain.entities.AboutEntity;
import com.sebastianhamm.Backend.event.domain.entities.EventEntity;
import com.sebastianhamm.Backend.location.domain.entities.LocationEntity;
import com.sebastianhamm.Backend.member.domain.enums.InstrumentEnum;
import com.sebastianhamm.Backend.member.domain.enums.SectionEnum;
import com.sebastianhamm.Backend.event.domain.enums.EventType;

import com.sebastianhamm.Backend.shared.domain.entities.Address;
import com.sebastianhamm.Backend.event.domain.enums.EventType;
import com.sebastianhamm.Backend.member.domain.enums.InstrumentEnum;
import com.sebastianhamm.Backend.member.domain.enums.SectionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("!prod")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final GalleryRepository galleryRepository;
    private final WelcomeRepository welcomeRepository;
    private final AboutRepository aboutRepository;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    public DataInitializer(MemberRepository memberRepository,
                          ImageRepository imageRepository,
                          GalleryRepository galleryRepository,
                          WelcomeRepository welcomeRepository,
                          AboutRepository aboutRepository,
                          EventRepository eventRepository,
                          LocationRepository locationRepository) {
        this.memberRepository = memberRepository;
        this.imageRepository = imageRepository;
        this.galleryRepository = galleryRepository;
        this.welcomeRepository = welcomeRepository;
        this.aboutRepository = aboutRepository;
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (memberRepository.count() > 0) {
            log.info("🔁 Members existieren bereits – Initialisierung übersprungen.");
        } else {
            log.info("▶️ Initialisiere Test-Mitglieder...");

            List<MemberEntity> members = List.of(
                    createMember("Anna", "Klar", InstrumentEnum.CLARINET, SectionEnum.WOOD, "https://stadtkapelle-eisenstadt.at/avatars/anna.jpg"),
                    createMember("Peter", "Blech", InstrumentEnum.TRUMPET, SectionEnum.HIGH_METAL, "https://stadtkapelle-eisenstadt.at/avatars/peter.jpg"),
                    createMember("Lena", "Schlag", InstrumentEnum.DRUMS, SectionEnum.DRUMS, "https://stadtkapelle-eisenstadt.at/avatars/lena.jpg")
            );

            memberRepository.saveAll(members);
            log.info("✅ {} Mitglieder gespeichert", members.size());
        }

        if (galleryRepository.count() > 0) {
            log.info("🔁 Galleries existieren bereits – Initialisierung übersprungen.");
        } else {
            log.info("▶️ Initialisiere Test-Galleries mit Bildern...");

            GalleryEntity gallery1 = new GalleryEntity();
            gallery1.setTitle("Sommerfest 2025");
            gallery1.setGalleryDate(LocalDate.of(2025, 6, 20));

            ImageEntity image1 = createImage(
                    "/uploads/images/sommerfest/1.jpg",
                    "Fotograf Sommerfest",
                    "sommerfest_1.jpg",
                    "image/jpeg",
                    120_000L,
                    LocalDate.of(2025, 6, 21),
                    gallery1);

            ImageEntity image2 = createImage(
                    "/uploads/images/sommerfest/2.jpg",
                    "Fotograf Sommerfest",
                    "sommerfest_2.jpg",
                    "image/jpeg",
                    95_000L,
                    LocalDate.of(2025, 6, 21),
                    gallery1);

            gallery1.getImages().add(image1);
            gallery1.getImages().add(image2);

            galleryRepository.save(gallery1);
            log.info("✅ Gallery '{}' mit {} Bildern gespeichert", gallery1.getTitle(), gallery1.getImages().size());
        }

        if (welcomeRepository.count() > 0) {
            log.info("🔁 Welcome-Daten existieren bereits – Initialisierung übersprungen.");
        } else {
            log.info("▶️ Initialisiere Welcome-Daten...");

            WelcomeEntity welcomeEntity = new WelcomeEntity();
            welcomeEntity.setTitle("Willkommen bei der Stadtkapelle Eisenstadt");
            welcomeEntity.setSubTitle("Hier finden Sie alle Informationen zu unseren Mitgliedern und Veranstaltungen.");
            welcomeEntity.setButtonText("Mehr erfahren");
            welcomeEntity.setBackgroundImageUrl("https://stadtkapelle-eisenstadt.at/images/welcome-bg.jpg");
            welcomeEntity.setUpdatedAt(LocalDateTime.now());
            welcomeRepository.save(welcomeEntity);
            log.info("✅ Welcome-Daten initialisiert.");
        }

        if (aboutRepository.count() > 0) {
            log.info("🔁 About-Daten existieren bereits – Initialisierung übersprungen.");
        } else {
            log.info("▶️ Initialisiere About-Daten...");

            AboutEntity aboutEntity = new AboutEntity();
            aboutEntity.setAboutText("Die Stadtkapelle Eisenstadt ist ein traditionsreicher Musikverein, der seit über 100 Jahren besteht. Wir freuen uns, Sie auf unserer Website begrüßen zu dürfen!");
            aboutEntity.setAboutImageUrl("https://stadtkapelle-eisenstadt.at/images/about.jpg");
            aboutEntity.setUpdatedAt(LocalDateTime.now());
            aboutRepository.save(aboutEntity);
            log.info("✅ About-Daten initialisiert.");
        }

        if (locationRepository.count() > 0) {
            log.info("🔁 Locations existieren bereits – Initialisierung übersprungen.");
        } else {
            log.info("▶️ Initialisiere Locations...");

            LocationEntity locationEntity = new LocationEntity();
            locationEntity.setName("Stadthalle Eisenstadt");
            Address address = new Address();
            address.setStreetNumber("1");
            address.setStreet("Eisenstadt");
            address.setPostalCode("7000");
            address.setCountry("Österreich");
            locationEntity.setAddress(address);
            locationRepository.save(locationEntity);
            log.info("✅ Location '{}' initialisiert.", locationEntity.getName());
        }

        if (eventRepository.count() > 0) {
            log.info("🔁 Event-Daten existieren bereits – Initialisierung übersprungen.");
        } else {
            EventEntity eventEntity = new EventEntity();
            eventEntity.setTitle("Jahreskonzert 2025");
            eventEntity.setDescription("Erleben Sie unser Jahreskonzert mit einem abwechslungsreichen Programm aus klassischer und moderner Blasmusik.");
            eventEntity.setDate(LocalDate.of(2025, 11, 15));
            eventEntity.setEventImageUrl("https://stadtkapelle-eisenstadt.at/images/event.jpg");
            eventEntity.setEventType(EventType.GIG);
            LocationEntity existingLocation = locationRepository
                    .findByName("Stadthalle Eisenstadt")
                    .orElseThrow(() -> new RuntimeException("Location nicht gefunden"));
            eventEntity.setLocation(existingLocation);
            eventEntity.setUpdatedAt(LocalDateTime.now());
            eventRepository.save(eventEntity);
            log.info("✅ Event '{}' initialisiert.", eventEntity.getTitle());
        }
    }

    private MemberEntity createMember(String firstName, String lastName,
                                      InstrumentEnum instrument, SectionEnum section, String avatarUrl) {
        MemberEntity member = new MemberEntity();
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setInstrument(instrument);
        member.setSection(section);
        member.setAvatarUrl(avatarUrl);
        member.setDateJoined(LocalDate.now().minusYears(1)); // Beispiel: Vor einem Jahr beigetreten
        member.setNotes(null);
        member.setPhoneNumber(null);

        Address address = new Address();
        address.setStreetNumber("1");
        address.setStreet("Eisenstadt");
        address.setPostalCode("7000");
        address.setCountry("Österreich");
        member.setAddress(address);
        return member;
    }

    private ImageEntity createImage(String imageUrl, String author, String filename, String mimeType,
                                    Long fileSizeBytes, LocalDate uploadDate, GalleryEntity gallery) {
        ImageEntity image = new ImageEntity();
        image.setImageUrl(imageUrl);
        image.setAuthor(author);
        image.setFilename(filename);
        image.setMimeType(mimeType);
        image.setFileSizeBytes(fileSizeBytes);
        image.setUploadDate(uploadDate);
        image.setGallery(gallery);
        return image;
    }
}
