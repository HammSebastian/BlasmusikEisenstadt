package at.sebastianhamm.kapelle_eisenstadt.service;

import at.sebastianhamm.kapelle_eisenstadt.dto.GigRequest;
import at.sebastianhamm.kapelle_eisenstadt.dto.GigResponse;

import java.util.List;

public interface GigService {
    List<GigResponse> findAll();
    GigResponse findById(Long id);
    GigResponse save(GigRequest gigRequest);
    GigResponse update(Long id, GigRequest gigRequest);
    void delete(Long id);
}
