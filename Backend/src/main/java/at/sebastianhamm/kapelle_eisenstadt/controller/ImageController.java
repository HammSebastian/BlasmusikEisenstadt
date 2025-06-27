package at.sebastianhamm.kapelle_eisenstadt.controller;

import at.sebastianhamm.kapelle_eisenstadt.dto.ImageDto;
import at.sebastianhamm.kapelle_eisenstadt.service.ImageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping
    public List<ImageDto> findAll() {
        return imageService.findAll();
    }

    @GetMapping("/{id}")
    public ImageDto findById(@PathVariable Long id) {
        return imageService.findById(id);
    }

    @GetMapping("/category/{category}")
    public ImageDto findByCategory(@PathVariable String category) {
        return imageService.findByCategory(category);
    }

    @PostMapping
    public ImageDto save(@Valid @RequestBody ImageDto imageDto) {
        return imageService.save(imageDto);
    }

    @PutMapping("/{id}")
    public ImageDto update(@PathVariable Long id, @Valid @RequestBody ImageDto imageDto) {
        return imageService.update(imageDto);
    }

    @DeleteMapping("/{id}")
    public Void delete(@PathVariable Long id) {
        imageService.delete(id);
        return null;
    }
}
