package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.exception.ResourceNotFoundException;
import at.sebastianhamm.backend.models.announcement.Announcement;
import at.sebastianhamm.backend.models.common.enums.EType;
import at.sebastianhamm.backend.payload.response.AnnouncementsResponse;
import at.sebastianhamm.backend.services.AnnouncementsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing announcements.
 *
 * Public GET endpoints accessible without authentication.
 * POST, PUT, DELETE endpoints restricted to ADMIN or REPORTER roles.
 */
@CrossOrigin(origins = {"https://stadtkapelle-eisenstadt.at", "http://localhost:4200"}, maxAge = 3600)
@RestController
@RequestMapping("/public/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementsService announcementsService;

    /**
     * Retrieve all announcements.
     * @return list of announcement DTOs
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AnnouncementsResponse> getAllAnnouncements() {
        return announcementsService.getAllAnnouncements();
    }

    /**
     * Retrieve announcement by its ID.
     * @param id announcement ID
     * @return announcement DTO
     * @throws ResourceNotFoundException if announcement not found
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnnouncementsResponse getAnnouncementById(@PathVariable Long id) {
        return announcementsService.getAnnouncementById(id)
                .map(announcementsService::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + id));
    }

    /**
     * Create a new announcement.
     * Access restricted to users with ADMIN or REPORTER role.
     * @param announcement announcement data
     * @return created announcement DTO
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPORTER')")
    @ResponseStatus(HttpStatus.CREATED)
    public AnnouncementsResponse createAnnouncement(@Valid @RequestBody Announcement announcement) {
        return announcementsService.createAnnouncement(announcement);
    }

    /**
     * Update existing announcement by ID.
     * Access restricted to users with ADMIN or REPORTER role.
     * @param id announcement ID
     * @param announcement updated announcement data
     * @return updated announcement DTO
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPORTER')")
    @ResponseStatus(HttpStatus.OK)
    public AnnouncementsResponse updateAnnouncement(
            @PathVariable Long id,
            @Valid @RequestBody Announcement announcement) {
        return announcementsService.updateAnnouncement(id, announcement);
    }

    /**
     * Delete announcement by ID.
     * Access restricted to users with ADMIN or REPORTER role.
     * @param id announcement ID
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPORTER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnnouncement(@PathVariable Long id) {
        announcementsService.deleteAnnouncement(id);
    }

    /**
     * Retrieve announcements filtered by type.
     * @param type announcement type
     * @return list of announcements matching the type
     */
    @GetMapping("/type/{type}")
    @ResponseStatus(HttpStatus.OK)
    public List<Announcement> getAnnouncementsByType(@PathVariable EType type) {
        return announcementsService.findByType(type);
    }
}
