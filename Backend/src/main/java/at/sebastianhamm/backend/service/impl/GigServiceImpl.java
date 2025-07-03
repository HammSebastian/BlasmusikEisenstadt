package at.sebastianhamm.backend.service.impl;

import at.sebastianhamm.backend.entity.GigEntity;
import at.sebastianhamm.backend.exception.ConflictException;
import at.sebastianhamm.backend.io.GigRequest;
import at.sebastianhamm.backend.io.GigResponse;
import at.sebastianhamm.backend.repository.GigRepository;
import at.sebastianhamm.backend.service.GigService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GigServiceImpl implements GigService {

    private final GigRepository gigRepository;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(GigServiceImpl.class);

    @Override
    public GigResponse createGig(GigRequest request) {
        if (gigRepository.existsByTitleIgnoreCase(request.getTitle())) {
            logger.error("Gig with title '{}' already exists", request.getTitle());
            throw new ConflictException("Gig with this title already exists");
        }
        GigEntity entity = mapToEntity(request);
        GigEntity saved = gigRepository.save(entity);
        return mapToResponse(saved);
    }

    @Override
    public GigResponse getGigById(Long id) {
        return gigRepository.findGigEntityById(id);
    }

    @Override
    public List<GigResponse> getAllGigs() {
        return gigRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<GigResponse> getGigsByDate(LocalDate date) {
        return gigRepository.findGigEntitiesByDate(date).stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public List<GigResponse> getGigsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return gigRepository.findGigEntitiesByDateBetween(startDate, endDate).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public GigResponse updateGig(Long id, GigRequest request) {
        GigEntity gig = gigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gig not found with id: " + id));

        gig.setTitle(request.getTitle());
        gig.setDescription(request.getDescription());
        gig.setLocation(request.getLocation());
        gig.setDate(request.getDate());
        gig.setStartTime(request.getStartTime());
        gig.setEndTime(request.getEndTime());

        GigEntity updated = gigRepository.save(gig);
        return mapToResponse(updated);
    }

    @Override
    public void deleteGigById(Long id) {
        if (!gigRepository.existsById(id)) {
            throw new RuntimeException("Gig not found with id: " + id);
        }
        gigRepository.deleteById(id);
    }

    @Override
    public List<GigResponse> getGigsByLocation(String location) {
        return gigRepository.findGigEntitiesByLocation(location).stream().map(this::mapToResponse).toList();
    }

    private GigEntity mapToEntity(GigRequest req) {
        if (req == null) throw new IllegalArgumentException("GigRequest must not be null");
        return GigEntity.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .location(req.getLocation())
                .date(req.getDate())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .build();
    }

    private GigResponse mapToResponse(GigEntity entity) {
        String time = null;
        if (entity.getStartTime() != null && entity.getEndTime() != null) {
            time = entity.getStartTime() + " - " + entity.getEndTime();
        } else if (entity.getStartTime() != null) {
            time = entity.getStartTime().toString();
        }

        return GigResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .location(entity.getLocation())
                .date(entity.getDate())
                .time(time)
                .build();
    }
}
