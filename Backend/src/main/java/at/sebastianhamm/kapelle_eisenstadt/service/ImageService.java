package at.sebastianhamm.kapelle_eisenstadt.service;

import at.sebastianhamm.kapelle_eisenstadt.dto.ImageDto;

import java.util.List;

public interface ImageService {
    ImageDto save(ImageDto imageDto);

    List<ImageDto> findAll();

    ImageDto findById(Long id);

    void delete(Long id);

    ImageDto update(ImageDto imageDto);

    ImageDto findByCategory(String category);

    ImageDto findByAuthor(String author);
}
