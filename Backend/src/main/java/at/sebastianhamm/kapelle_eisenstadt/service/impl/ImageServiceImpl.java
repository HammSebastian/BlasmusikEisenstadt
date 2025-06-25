package at.sebastianhamm.kapelle_eisenstadt.service.impl;

import at.sebastianhamm.kapelle_eisenstadt.dao.ImageDao;
import at.sebastianhamm.kapelle_eisenstadt.dto.ImageRequest;
import at.sebastianhamm.kapelle_eisenstadt.dto.ImageResponse;
import at.sebastianhamm.kapelle_eisenstadt.entity.Image;
import at.sebastianhamm.kapelle_eisenstadt.exception.ResourceNotFoundException;
import at.sebastianhamm.kapelle_eisenstadt.service.ImageService;
import at.sebastianhamm.kapelle_eisenstadt.util.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    private final ImageDao imageDao;

    @Autowired
    public ImageServiceImpl(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public List<ImageResponse> findAll() {
        return imageDao.findAll().stream()
                .map(DtoMapper::toImageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ImageResponse findById(Long id) {
        Image image = imageDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", id));
        return DtoMapper.toImageResponse(image);
    }

    @Override
    public List<ImageResponse> findByCategory(String category) {
        return imageDao.findImagesByImageCategory(category).stream()
                .map(DtoMapper::toImageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ImageResponse save(ImageRequest imageRequest) {
        Image image = DtoMapper.toImage(imageRequest);
        Image savedImage = imageDao.save(image);
        return DtoMapper.toImageResponse(savedImage);
    }

    @Override
    public ImageResponse update(Long id, ImageRequest imageRequest) {
        Image existingImage = imageDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", id));
        
        existingImage.setImagePath(imageRequest.getImagePath());
        existingImage.setImageCategory(imageRequest.getImageCategory());
        existingImage.setImageAuthor(imageRequest.getImageAuthor());
        
        Image updatedImage = imageDao.save(existingImage);
        return DtoMapper.toImageResponse(updatedImage);
    }

    @Override
    public void delete(Long id) {
        Image image = imageDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", id));
        imageDao.delete(image);
    }
}
