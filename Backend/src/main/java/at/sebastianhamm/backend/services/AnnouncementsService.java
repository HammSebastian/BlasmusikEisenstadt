package at.sebastianhamm.backend.services;

import at.sebastianhamm.backend.models.announcement.Announcement;
import at.sebastianhamm.backend.models.common.enums.EType;
import at.sebastianhamm.backend.payload.response.AnnouncementsResponse;

import java.util.List;
import java.util.Optional;

public interface AnnouncementsService {

    List<AnnouncementsResponse> getAllAnnouncements();
    Optional<Announcement> getAnnouncementById(Long id);
    AnnouncementsResponse createAnnouncement(Announcement announcement);
    AnnouncementsResponse updateAnnouncement(Long id, Announcement announcement);
    void deleteAnnouncement(Long id);
    List<Announcement> findByType(EType type);
    
    AnnouncementsResponse mapToResponse(Announcement announcement);
}
