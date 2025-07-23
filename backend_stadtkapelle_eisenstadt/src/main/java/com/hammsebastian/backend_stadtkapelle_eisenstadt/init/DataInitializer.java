/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.init;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.*;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.EventType;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.InstrumentEnum;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.NewsType;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.SectionEnum;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final WelcomeContentRepository welcomeContentRepository;
    private final AboutRepository aboutRepository;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final NewsRepository newsRepository;
    private final HistoryRepository historyRepository;
    private final SectionRepository sectionRepository;
    private final MemberRepository memberRepository;
    private final GalleryRepository galleryRepository;
    private final ImageRepository imageRepository;

    @Override
    public void run(String... args) {

        // WelcomeContent
        if (welcomeContentRepository.count() == 0) {
            WelcomeEntity welcome = WelcomeEntity.builder()
                    .title("Stadtkapelle Eisenstadt")
                    .subTitle("Tradition seit 1874")
                    .buttonText("Unsere nächsten Auftritte")
                    .backgroundImage("assets/images/Hero-Image.jpeg")
                    .build();
            welcomeContentRepository.save(welcome);
            log.info("WelcomeContent saved");
        }

        // About
        if (aboutRepository.count() == 0) {
            AboutEntity about = AboutEntity.builder()
                    .aboutText("Wir sind die Stadtkapelle Eisenstadt – Musik, Leidenschaft, Gemeinschaft.")
                    .aboutImage("assets/images/Hero-Image.jpeg")
                    .build();
            aboutRepository.save(about);
            log.info("About saved");
        }

        // Locations
        if (locationRepository.count() == 0) {
            for (int i = 1; i <= 5; i++) {
                LocationEntity location = LocationEntity.builder()
                        .country("Austria")
                        .zipCode("7000" + i)
                        .city("City " + i)
                        .street("Street " + i)
                        .number(String.valueOf(i))
                        .build();
                locationRepository.save(location);
            }
            log.info("5 Locations saved");
        }

        // Events
        if (eventRepository.count() == 0) {
            for (int i = 1; i <= 5; i++) {
                EventEntity event = EventEntity.builder()
                        .title("Jahreskonzert " + i)
                        .description("Unser traditionsreiches Konzert Nummer " + i)
                        .date(LocalDate.now().plusDays(i * 30))
                        .eventImage("assets/images/Hero-Image.jpeg")
                        .eventType(EventType.CONCERT)
                        .location(locationRepository.findById((long) i).orElse(null))
                        .build();
                eventRepository.save(event);
            }
            log.info("5 Events saved");
        }

        // News
        if (newsRepository.count() == 0) {
            for (int i = 1; i <= 5; i++) {
                NewEntity news = NewEntity.builder()
                        .title("Neuigkeit " + i)
                        .description("Spannende Information Nummer " + i)
                        .newsImage("assets/images/Hero-Image.jpeg")
                        .newsType(NewsType.ANNOUNCEMENT)
                        .date(LocalDate.now().minusDays(i * 7))
                        .isPublished(i <= 3) // 3 veröffentlicht
                        .build();
                newsRepository.save(news);
            }
            log.info("5 News saved");
        }

        // History + Sections
        if (historyRepository.count() == 0) {
            HistoryEntity history = HistoryEntity.builder()
                    .name("Stadtkapelle Eisenstadt")
                    .sections(Collections.emptyList())
                    .build();
            history = historyRepository.save(history);

            for (int i = 0; i < 5; i++) {
                SectionEntity section = SectionEntity.builder()
                        .year(1874 + (i * 30))
                        .description("Historischer Meilenstein im Jahr " + (1874 + (i * 30)))
                        .history(history)
                        .build();
                sectionRepository.save(section);
            }
            log.info("History + 5 Sections saved");
        }

        // Members
        if (memberRepository.count() == 0) {
            List<MemberEntity> members = List.of(
                    MemberEntity.builder()
                            .firstName("Sebastian").lastName("Hamm").instrument(InstrumentEnum.TRUMPET)
                            .section(SectionEnum.HIGH_METAL).dateJoined(LocalDate.of(2015, 9, 1))
                            .avatarUrl("assets/images/Hero-Image.jpeg").build(),
                    MemberEntity.builder()
                            .firstName("Anna").lastName("Müller").instrument(InstrumentEnum.FLUTE)
                            .section(SectionEnum.WOOD).dateJoined(LocalDate.of(2018, 3, 15))
                            .avatarUrl("assets/images/Hero-Image.jpeg").build(),
                    MemberEntity.builder().firstName("Lukas").lastName("Schmidt").instrument(InstrumentEnum.CLARINET)
                            .section(SectionEnum.WOOD).dateJoined(LocalDate.of(2020, 7, 10))
                            .avatarUrl("assets/images/Hero-Image.jpeg").build(),
                    MemberEntity.builder().firstName("Maria").lastName("Weber").instrument(InstrumentEnum.DRUMS)
                            .section(SectionEnum.DRUMS).dateJoined(LocalDate.of(2017, 5, 5))
                            .avatarUrl("assets/images/Hero-Image.jpeg").build(),
                    MemberEntity.builder().firstName("David").lastName("Gruber").instrument(InstrumentEnum.TUBA)
                            .section(SectionEnum.LOW_METAL).dateJoined(LocalDate.of(2016, 11, 20))
                            .avatarUrl("assets/images/Hero-Image.jpeg").build()
            );
            memberRepository.saveAll(members);
            log.info("5 Members saved");
        }

        // Gallery
        if (galleryRepository.count() == 0) {
            // First, create the GalleryEntity objects (without saving them yet)
            GalleryEntity gallery1 = GalleryEntity.builder()
                    .title("Stadtkapelle Eisenstadt")
                    .fromDate(LocalDate.now())
                    .images(List.of())
                    .build();
            
            GalleryEntity gallery2 = GalleryEntity.builder()
                    .title("Stadtkapelle Eisenstadt1")
                    .fromDate(LocalDate.now())
                    .images(List.of())
                    .build();
            
            GalleryEntity gallery3 = GalleryEntity.builder()
                    .title("Stadtkapelle Eisenstadt2")
                    .fromDate(LocalDate.now())
                    .images(List.of())
                    .build();
            
            // Create the ImageEntity objects and set their gallery field
            ImageEntity image1 = ImageEntity.builder()
                    .imageUrl("assets/images/Hero-Image.jpeg")
                    .author("Sebastian Hamm")
                    .date(LocalDate.now())
                    .gallery(gallery1)
                    .build();
            
            ImageEntity image2 = ImageEntity.builder()
                    .imageUrl("assets/images/Hero-Image.jpeg")
                    .author("Sebastian Hamm1")
                    .date(LocalDate.now())
                    .gallery(gallery2)
                    .build();
            
            ImageEntity image3 = ImageEntity.builder()
                    .imageUrl("assets/images/Hero-Image.jpeg")
                    .author("Sebastian Hamm2")
                    .date(LocalDate.now())
                    .gallery(gallery3)
                    .build();
            
            // Set the images list for each gallery
            gallery1.setImages(List.of(image1));
            gallery2.setImages(List.of(image2));
            gallery3.setImages(List.of(image3));
            
            // Save the galleries, which will cascade the save operation to the images
            galleryRepository.saveAll(List.of(gallery1, gallery2, gallery3));
            log.info("3 Galleries with images saved");
        }
    }
}