package at.sebastianhamm.kapelle_eisenstadt.service.impl;

import at.sebastianhamm.kapelle_eisenstadt.dao.ImageDao;
import at.sebastianhamm.kapelle_eisenstadt.dto.ImageDto;
import at.sebastianhamm.kapelle_eisenstadt.exception.ImageAlreadyExistsException;
import at.sebastianhamm.kapelle_eisenstadt.models.Image;
import at.sebastianhamm.kapelle_eisenstadt.service.ImageService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service(value = "imageService")
public class ImageServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private ImageDao imageDao;

    @Override
    public List<ImageDto> findAll() {
        logger.debug("Retrieving user list");
        List<ImageDto> list = new ArrayList<>();
        imageDao.findAll().iterator().forEachRemaining(u -> list.add(map(u)));
        return list;
    }

    @Override
    public ImageDto save(ImageDto imageDto) {
        Validate.notNull(imageDto);
        Validate.notNull(imageDto.getPath(), "path must not be null");

        logger.info("Saving image new image {}",imageDto.getPath());

        Optional<Image> image = imageDao.findImageByPath(imageDto.getPath());
        if(image.isPresent()){
            logger.warn("Image {} already exists", imageDto.getPath());
            throw new ImageAlreadyExistsException("Image already exists!");
        }

        Image newImage = new Image();
        BeanUtils.copyProperties(imageDto, newImage);
        newImage = imageDao.save(newImage);
        return map(newImage);
    }

    @Override
    public ImageDto findById(Long id) {
        Validate.notNull(id,"id must not be null");
        logger.debug("Find user by id {}",id);
        Optional<Image> optionalImage = imageDao.findById(id);
        return optionalImage.map(this::map).orElse(null);
    }

    @Override
    public void delete(Long id) {
        Validate.notNull(id, "id must not be null");
        logger.info("Deleting user with id {}",id);
        imageDao.deleteById(id);
    }

    @Override
    public ImageDto update(ImageDto imageDto) {
        Validate.notNull(imageDto);
        Validate.notNull(imageDto.getId(), "userDto.id must not be null");
        logger.info("Updating user {}",imageDto.getId());

        Optional<Image> optionalImage = imageDao.findById(imageDto.getId());
        Validate.isTrue(optionalImage.isPresent());
        Image image = optionalImage.get();
        BeanUtils.copyProperties(imageDto, image, "password");
        imageDao.save(image);
        return imageDto;
    }

    @Override
    public ImageDto findByCategory(String category) {
        Validate.notNull(category, "category must not be null");
        logger.debug("Find user by category {}",category);
        Optional<Image> optionalImage = imageDao.findImageByCategory(category);
        return optionalImage.map(this::map).orElse(null);
    }

    @Override
    public ImageDto findByAuthor(String author) {
        Validate.notNull(author, "author must not be null");
        logger.debug("Find user by author {}",author);
        Optional<Image> optionalImage = imageDao.findImageByAuthor(author);
        return optionalImage.map(this::map).orElse(null);
    }

    private ImageDto map(Image image){
        ImageDto imageDto = new ImageDto();
        BeanUtils.copyProperties(image, imageDto);
        return imageDto;
    }
}
