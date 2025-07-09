package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.models.announcement.About;
import at.sebastianhamm.backend.payload.response.AboutResponse;
import at.sebastianhamm.backend.services.AboutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller für den Zugriff auf About-Informationen.
 * <p>
 * Öffentliche GET-Anfrage ohne Authentifizierung.
 * PUT-Anfrage nur für Benutzer mit ADMIN oder REPORTER Rolle.
 * </p>
 */
@CrossOrigin(origins = {"https://stadtkapelle-eisenstadt.at", "http://localhost:4200"}, maxAge = 3600)
@RestController
@RequestMapping("/public/about")
@RequiredArgsConstructor
public class AboutController {

    private final AboutService aboutService;

    /**
     * Liefert die About-Informationen zurück.
     *
     * @return AboutResponse mit den aktuellen About-Daten
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AboutResponse getAbout() {
        return aboutService.getAbout();
    }

    /**
     * Aktualisiert die About-Informationen.
     * Nur für Benutzer mit Rolle ADMIN oder REPORTER erlaubt.
     *
     * @param about About-Objekt mit neuen Daten, validiert
     * @return Aktualisierte AboutResponse
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPORTER')")
    @ResponseStatus(HttpStatus.OK)
    public AboutResponse updateAbout(@Valid @RequestBody About about) {
        return aboutService.updateAbout(about);
    }
}
