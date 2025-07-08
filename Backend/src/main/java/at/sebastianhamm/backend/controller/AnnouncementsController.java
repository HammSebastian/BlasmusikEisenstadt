package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.exception.ResourceNotFoundException;
import at.sebastianhamm.backend.models.Announcement;
import at.sebastianhamm.backend.models.EType;
import at.sebastianhamm.backend.payload.response.AnnouncementsResponse;
import at.sebastianhamm.backend.services.AnnouncementsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/public/announcements")
@Tag(name = "Announcements", description = "The announcements endpoint")
@RequiredArgsConstructor
public class AnnouncementsController {

    private final AnnouncementsService announcementsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AnnouncementsResponse> getAllAnnouncements() {
        return announcementsService.getAllAnnouncements();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnnouncementsResponse getAnnouncementById(@PathVariable Long id) {
        return announcementsService.getAnnouncementById(id)
                .map(announcementsService::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_REPORTER')")
    @ResponseStatus(HttpStatus.CREATED)
    public AnnouncementsResponse createAnnouncement(@Valid @RequestBody Announcement announcement) {
        return announcementsService.createAnnouncement(announcement);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_REPORTER')")
    @ResponseStatus(HttpStatus.OK)
    public AnnouncementsResponse updateAnnouncement(
            @PathVariable Long id,
            @Valid @RequestBody Announcement announcement) {
        return announcementsService.updateAnnouncement(id, announcement);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_REPORTER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnnouncement(@PathVariable Long id) {
        announcementsService.deleteAnnouncement(id);
    }

    @GetMapping("/type/{type}")
    @ResponseStatus(HttpStatus.OK)
    public List<Announcement> getAnnouncementsByType(@PathVariable EType type) {
        return announcementsService.findByType(type);
    }
}
