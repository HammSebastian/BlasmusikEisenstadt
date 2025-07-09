package at.sebastianhamm.backend.services.impl;

import at.sebastianhamm.backend.exception.ConflictException;
import at.sebastianhamm.backend.models.announcement.Announcement;
import at.sebastianhamm.backend.models.common.enums.EType;
import at.sebastianhamm.backend.models.user.Type;
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
    public Optional<Announcement> getAnnouncementById(Long id) {
        return announcementsRepository.findById(id);
    }

    @Override
    public AnnouncementsResponse createAnnouncement(Announcement announcement) {
        if (announcement.getId() != null && announcementsRepository.existsById(announcement.getId())) {
            throw new ConflictException("Announcement with id " + announcement.getId() + " already exists");
        }

        Announcement saved = announcementsRepository.save(announcement);
        return mapToResponse(saved);
    }

    @Override
    public AnnouncementsResponse updateAnnouncement(Long id, Announcement announcement) {
        return announcementsRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(announcement.getTitle());
                    existing.setMessage(announcement.getMessage());
                    existing.setTypes(announcement.getTypes());
                    existing.setStartDate(announcement.getStartDate());
                    existing.setEndDate(announcement.getEndDate());
                    existing.setCreatedBy(announcement.getCreatedBy());
                    Announcement updated = announcementsRepository.save(existing);
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
    public List<Announcement> findByType(EType type) {
        Set<Type> types = Set.of(new Type(type));
        return announcementsRepository.findByTypes(types);
    }

    public AnnouncementsResponse mapToResponse(Announcement announcement) {
        return AnnouncementsResponse.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .message(announcement.getMessage())
                .types(announcement.getTypes().stream()
                        .map(type -> type.getType().name())
                        .collect(java.util.stream.Collectors.toSet()))
                .startDate(announcement.getStartDate())
                .endDate(announcement.getEndDate())
                .createdBy(announcement.getCreatedBy())
                .build();
    }
}
