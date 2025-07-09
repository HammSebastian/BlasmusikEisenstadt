package at.sebastianhamm.backend.services.impl;

import at.sebastianhamm.backend.exception.ConflictException;
import at.sebastianhamm.backend.models.gig.enums.EGigs;
import at.sebastianhamm.backend.models.gig.Gig;
import at.sebastianhamm.backend.models.gig.GigType;
import at.sebastianhamm.backend.payload.response.GigResponse;
import at.sebastianhamm.backend.repository.GigRepository;
import at.sebastianhamm.backend.services.GigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GigServiceImpl implements GigService {

    private final GigRepository gigRepository;

    @Override
    public List<GigResponse> getAllGigs() {
        return gigRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Optional<Gig> getGigById(Long id) {
        return gigRepository.findById(id);
    }

    @Override
    public GigResponse createGig(Gig gig) {
        if (gig.getId() != null && gigRepository.existsById(gig.getId())) {
            throw new ConflictException("Gig with id " + gig.getId() + " already exists");
        }

        Gig saved = gigRepository.save(gig);
        return mapToResponse(saved);
    }

    @Override
    public GigResponse updateGig(Long id, Gig gig) {
        return gigRepository.findById(id).map(existing -> {
            existing.setTitle(gig.getTitle());
            existing.setDescription(gig.getDescription());
            existing.setGigTypes(gig.getGigTypes());
            existing.setVenue(gig.getVenue());
            existing.setAddress(gig.getAddress());
            existing.setImageUrl(gig.getImageUrl());
            existing.setAdditionalInfo(gig.getAdditionalInfo());
            existing.setDate(gig.getDate());
            existing.setTime(gig.getTime());
            Gig updated = gigRepository.save(existing);
            return mapToResponse(updated);
        }).orElseThrow(() -> new ConflictException("Gig with id " + id + " does not exist"));
    }

    @Override
    public void deleteGig(Long id) {
        if (!gigRepository.existsById(id)) {
            throw new ConflictException("Gig with id " + id + " does not exist");
        }

        gigRepository.deleteById(id);
    }

    @Override
    public List<Gig> findByType(EGigs type) {
        Set<GigType> types = Set.of(new GigType(type));
        return gigRepository.findByGigTypesIn(types);
    }

    @Override
    public GigResponse mapToResponse(Gig gig) {
        return GigResponse.builder()
                .id(gig.getId())
                .title(gig.getTitle())
                .description(gig.getDescription())
                .venue(gig.getVenue())
                .address(gig.getAddress())
                .imageUrl(gig.getImageUrl())
                .additionalInfo(gig.getAdditionalInfo())
                .date(gig.getDate())
                .time(gig.getTime())
                .types(gig.getGigTypes().stream()
                        .map(type -> type.getEgigs().name())
                        .collect(java.util.stream.Collectors.toSet()))
                .build();
    }
}
