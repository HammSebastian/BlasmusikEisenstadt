package at.sebastianhamm.backend.services.impl;

import at.sebastianhamm.backend.exception.ConflictException;
import at.sebastianhamm.backend.models.Announcements;
import at.sebastianhamm.backend.models.EType;
import at.sebastianhamm.backend.models.Type;
import at.sebastianhamm.backend.payload.response.AnnouncementsResponse;
import at.sebastianhamm.backend.repository.AnnouncementsRepository;
import at.sebastianhamm.backend.services.AnnouncementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AnnouncementsServiceImpl implements AnnouncementsService {

    private final AnnouncementsRepository announcementsRepository;

    @Override
    public List<AnnouncementsResponse> getAllAnnouncements() {
        return announcementsRepository.findAllWithTypes().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Optional<Announcements> getAnnouncementById(Long id) {
        return announcementsRepository.findById(id);
    }

    @Override
    public AnnouncementsResponse createAnnouncement(Announcements announcement) {
        if (announcement.getId() != null && announcementsRepository.existsById(announcement.getId())) {
            throw new ConflictException("Announcement with id " + announcement.getId() + " already exists");
        }

        Announcements saved = announcementsRepository.save(announcement);
        return mapToResponse(saved);
    }

    @Override
    public AnnouncementsResponse updateAnnouncement(Long id, Announcements announcement) {
        return announcementsRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(announcement.getTitle());
                    existing.setMessage(announcement.getMessage());
                    existing.setTypes(announcement.getTypes());
                    existing.setStartDate(announcement.getStartDate());
                    existing.setEndDate(announcement.getEndDate());
                    existing.setCreatedBy(announcement.getCreatedBy());
                    Announcements updated = announcementsRepository.save(existing);
                    return mapToResponse(updated);
                })
                .orElseThrow(() -> new ConflictException("Announcement with id " + id + " does not exist"));
    }

    @Override
    public void deleteAnnouncement(Long id) {
        if (!announcementsRepository.existsById(id)) {
            throw new ConflictException("Announcement with id " + id + " does not exist");
        }
        announcementsRepository.deleteById(id);
    }

    @Override
    public List<Announcements> findByType(EType type) {
        Set<Type> types = Set.of(new Type(type));
        return announcementsRepository.findByTypes(types);
    }

    public AnnouncementsResponse mapToResponse(Announcements announcements) {
        return AnnouncementsResponse.builder()
                .id(announcements.getId())
                .title(announcements.getTitle())
                .message(announcements.getMessage())
                .types(announcements.getTypes().stream()
                        .map(type -> type.getType().name())
                        .collect(java.util.stream.Collectors.toSet()))
                .startDate(announcements.getStartDate())
                .endDate(announcements.getEndDate())
                .createdBy(announcements.getCreatedBy())
                .build();
    }
}
