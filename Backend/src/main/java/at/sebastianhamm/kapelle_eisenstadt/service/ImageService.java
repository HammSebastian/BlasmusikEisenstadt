package at.sebastianhamm.kapelle_eisenstadt.service;

import at.sebastianhamm.kapelle_eisenstadt.dto.ImageRequest;
import at.sebastianhamm.kapelle_eisenstadt.dto.ImageResponse;

import java.util.List;

public interface ImageService {
    List<ImageResponse> findAll();
    ImageResponse findById(Long id);
    List<ImageResponse> findByCategory(String category);
    ImageResponse save(ImageRequest imageRequest);
    ImageResponse update(Long id, ImageRequest imageRequest);
    void delete(Long id);
}
