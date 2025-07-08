package at.sebastianhamm.backend.services;

import at.sebastianhamm.backend.models.Announcements;
import at.sebastianhamm.backend.models.EType;
import at.sebastianhamm.backend.payload.response.AnnouncementsResponse;

import java.util.List;
import java.util.Optional;

public interface AnnouncementsService {

    List<AnnouncementsResponse> getAllAnnouncements();
    Optional<Announcements> getAnnouncementById(Long id);
    AnnouncementsResponse createAnnouncement(Announcements announcement);
    AnnouncementsResponse updateAnnouncement(Long id, Announcements announcement);
    void deleteAnnouncement(Long id);
    List<Announcements> findByType(EType type);
    
    AnnouncementsResponse mapToResponse(Announcements announcements);
}
