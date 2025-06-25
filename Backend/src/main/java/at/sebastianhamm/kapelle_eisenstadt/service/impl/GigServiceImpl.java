package at.sebastianhamm.kapelle_eisenstadt.service.impl;

import at.sebastianhamm.kapelle_eisenstadt.dao.GigDao;
import at.sebastianhamm.kapelle_eisenstadt.dto.GigRequest;
import at.sebastianhamm.kapelle_eisenstadt.dto.GigResponse;
import at.sebastianhamm.kapelle_eisenstadt.entity.Gig;
import at.sebastianhamm.kapelle_eisenstadt.exception.ResourceNotFoundException;
import at.sebastianhamm.kapelle_eisenstadt.service.GigService;
import at.sebastianhamm.kapelle_eisenstadt.util.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GigServiceImpl implements GigService {

    private final GigDao gigDao;

    @Autowired
    public GigServiceImpl(GigDao gigDao) {
        this.gigDao = gigDao;
    }

    @Override
    public List<GigResponse> findAll() {
        return gigDao.findAll().stream()
                .map(DtoMapper::toGigResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GigResponse findById(Long id) {
        Gig gig = gigDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gig", "id", id));
        return DtoMapper.toGigResponse(gig);
    }

    @Override
    public GigResponse save(GigRequest gigRequest) {
        Gig gig = DtoMapper.toGig(gigRequest);
        Gig savedGig = gigDao.save(gig);
        return DtoMapper.toGigResponse(savedGig);
    }

    @Override
    public GigResponse update(Long id, GigRequest gigRequest) {
        Gig existingGig = gigDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gig", "id", id));
        
        existingGig.setGigTitel(gigRequest.getGigTitel());
        existingGig.setGigDescription(gigRequest.getGigDescription());
        existingGig.setGigLocation(gigRequest.getGigLocation());
        existingGig.setGigDate(gigRequest.getGigDate());
        
        Gig updatedGig = gigDao.save(existingGig);
        return DtoMapper.toGigResponse(updatedGig);
    }

    @Override
    public void delete(Long id) {
        Gig gig = gigDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gig", "id", id));
        gigDao.delete(gig);
    }
}
