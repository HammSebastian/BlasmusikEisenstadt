package at.sebastianhamm.kapelle_eisenstadt.controller;

import at.sebastianhamm.kapelle_eisenstadt.dto.ImageRequest;
import at.sebastianhamm.kapelle_eisenstadt.dto.ImageResponse;
import at.sebastianhamm.kapelle_eisenstadt.service.ImageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<List<ImageResponse>> getAllImages() {
        return ResponseEntity.ok(imageService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageResponse> getImageById(@PathVariable Long id) {
        return ResponseEntity.ok(imageService.findById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ImageResponse>> getImagesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(imageService.findByCategory(category));
    }

    @PostMapping
    public ResponseEntity<ImageResponse> createImage(@Valid @RequestBody ImageRequest imageRequest) {
        return ResponseEntity.ok(imageService.save(imageRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImageResponse> updateImage(
            @PathVariable Long id,
            @Valid @RequestBody ImageRequest imageRequest) {
        return ResponseEntity.ok(imageService.update(id, imageRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
