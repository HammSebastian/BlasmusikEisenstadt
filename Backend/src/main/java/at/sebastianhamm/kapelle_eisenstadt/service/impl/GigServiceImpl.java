package at.sebastianhamm.kapelle_eisenstadt.service.impl;

import at.sebastianhamm.kapelle_eisenstadt.dao.GigDao;
import at.sebastianhamm.kapelle_eisenstadt.dto.GigDto;
import at.sebastianhamm.kapelle_eisenstadt.exception.GigAlreadyExistsException;
import at.sebastianhamm.kapelle_eisenstadt.models.Gig;
import at.sebastianhamm.kapelle_eisenstadt.service.GigService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "gigService")
public class GigServiceImpl implements GigService {

    private static final Logger logger = LoggerFactory.getLogger(GigServiceImpl.class);

    @Autowired
    private GigDao gigDao;

    @Override
    public List<GigDto> findAll() {
        logger.debug("Retrieving gig list");
        List<GigDto> list = new ArrayList<>();
        gigDao.findAll().iterator().forEachRemaining(u -> list.add(map(u)));
        return list;
    }

    @Override
    public GigDto findById(Long id) {
        Validate.notNull(id, "id must not be null");
        logger.debug("Find gig by id {}", id);
        Optional<Gig> optionalGig = gigDao.findById(id);
        return optionalGig.map(this::map).orElse(null);
    }

    @Override
    public GigDto save(GigDto gigDto) {
        Validate.notNull(gigDto);
        Validate.notNull(gigDto.getTitle(), "title must not be null");

        logger.info("Saving user new user {}", gigDto.getTitle());

        Optional<Gig> gig = gigDao.findGigByTitle(gigDto.getTitle());
        if (gig.isPresent()) {
            logger.warn("Gig {} already exists", gigDto.getTitle());
            throw new GigAlreadyExistsException("Gig already exists!");
        }

        Gig newGig = new Gig();
        BeanUtils.copyProperties(gigDto, newGig);
        newGig = gigDao.save(newGig);
        return map(newGig);
    }

    @Override
    public GigDto update(GigDto gigDto) {
        Validate.notNull(gigDto);
        Validate.notNull(gigDto.getId(), "gigDto.id must not be null");
        logger.info("Updating user {}", gigDto.getId());

        Optional<Gig> optionalGig = gigDao.findById(gigDto.getId());
        Validate.isTrue(optionalGig.isPresent());
        Gig gig = optionalGig.get();
        BeanUtils.copyProperties(gigDto, gig);
        gigDao.save(gig);
        return gigDto;
    }

    @Override
    public void delete(Long id) {
        Validate.notNull(id, "id must not be null");
        logger.info("Deleting gig with id {}",id);
        gigDao.deleteById(id);
    }

    private GigDto map(Gig gig) {
        GigDto gigDto = new GigDto();
        BeanUtils.copyProperties(gig, gigDto);
        return gigDto;
    }
}
