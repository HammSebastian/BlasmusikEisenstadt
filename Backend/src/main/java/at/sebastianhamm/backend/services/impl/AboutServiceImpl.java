package at.sebastianhamm.backend.services.impl;

import at.sebastianhamm.backend.exception.ConflictException;
import at.sebastianhamm.backend.models.announcement.About;
import at.sebastianhamm.backend.payload.response.AboutResponse;
import at.sebastianhamm.backend.payload.response.MissionResponse;
import at.sebastianhamm.backend.repository.AboutRepository;
import at.sebastianhamm.backend.services.AboutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class AboutServiceImpl implements AboutService {

    private final AboutRepository aboutRepository;

    private static final Long SINGLE_ABOUT_ID = 1L;

    @Override
    @Transactional(readOnly = true)
    public AboutResponse getAbout() {
        return aboutRepository.findByIdWithMissions(SINGLE_ABOUT_ID)
                .map(about -> {
                    about.getMissions().forEach(m -> System.out.println(" - " + m.getTitle()));
                    return mapToResponse(about);
                })
                .orElse(null);
    }


    @Override
    @Transactional
    public AboutResponse getOrCreateAbout() {
        return aboutRepository.findByIdWithMissions(SINGLE_ABOUT_ID)
                .or(() -> {
                    About newAbout = new About();
                    newAbout.setImageUrl("");
                    newAbout.setStory("");
                    return Optional.of(aboutRepository.save(newAbout));
                })
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Failed to get or create About"));
    }

    @Override
    @Transactional
    public AboutResponse updateAbout(About about) {
        return aboutRepository.findByIdWithMissions(SINGLE_ABOUT_ID)
                .map(existing -> {
                    // Update simple fields
                    existing.setImageUrl(about.getImageUrl());
                    existing.setStory(about.getStory());
                    
                    // Clear existing missions
                    existing.getMissions().clear();
                    
                    // Add all new missions with proper relationship management
                    about.getMissions().forEach(mission -> {
                        mission.setAbout(existing);  // Set the back reference
                        existing.getMissions().add(mission);
                    });
                    
                    return aboutRepository.save(existing);
                })
                .or(() -> {
                    return Optional.of(aboutRepository.save(about));
                })
                .map(this::mapToResponse)
                .orElseThrow(() -> new ConflictException("Failed to update About"));
    }

    @Override
    public AboutResponse mapToResponse(About about) {
        if (about == null) return null;

        about.getMissions().forEach(m -> System.out.println("Mission: " + m.getTitle()));

        return AboutResponse.builder()
                .id(about.getId())
                .imageUrl(about.getImageUrl())
                .story(about.getStory())
                .missions(about.getMissions().stream()
                        .map(MissionResponse::from)  // hier volle Objekte mappen
                        .collect(Collectors.toSet()))
                .build();
    }
}
