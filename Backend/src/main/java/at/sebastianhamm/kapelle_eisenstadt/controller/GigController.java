package at.sebastianhamm.kapelle_eisenstadt.controller;

import at.sebastianhamm.kapelle_eisenstadt.dto.GigRequest;
import at.sebastianhamm.kapelle_eisenstadt.dto.GigResponse;
import at.sebastianhamm.kapelle_eisenstadt.service.GigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gigs")
public class GigController {

    private final GigService gigService;

    @Autowired
    public GigController(GigService gigService) {
        this.gigService = gigService;
    }

    @GetMapping
    public ResponseEntity<List<GigResponse>> getAllGigs() {
        return ResponseEntity.ok(gigService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GigResponse> getGigById(@PathVariable Long id) {
        return ResponseEntity.ok(gigService.findById(id));
    }

    @PostMapping
    public ResponseEntity<GigResponse> createGig(@Valid @RequestBody GigRequest gigRequest) {
        return ResponseEntity.ok(gigService.save(gigRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GigResponse> updateGig(
            @PathVariable Long id, 
            @Valid @RequestBody GigRequest gigRequest) {
        return ResponseEntity.ok(gigService.update(id, gigRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGig(@PathVariable Long id) {
        gigService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
