package at.sebastianhamm.kapelle_eisenstadt.controller;

import at.sebastianhamm.kapelle_eisenstadt.dto.GigDto;
import at.sebastianhamm.kapelle_eisenstadt.service.GigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/gigs")
public class GigController {

    @Autowired
    private GigService gigService;

    @GetMapping
    public List<GigDto> findAll() {
        return gigService.findAll();
    }

    @GetMapping("/{id}")
    public GigDto findById(@PathVariable Long id) {
        return gigService.findById(id);
    }

    @PostMapping
    public GigDto saveGig(@Valid @RequestBody GigDto gigDto) {
        return gigService.save(gigDto);
    }

    @PutMapping("/{id}")
    public GigDto update(@Valid @RequestBody GigDto gigDto) {
        return gigService.update(gigDto);
    }

    @DeleteMapping("/{id}")
    public Void delete(@PathVariable Long id) {
        gigService.delete(id);
        return null;
    }
}
