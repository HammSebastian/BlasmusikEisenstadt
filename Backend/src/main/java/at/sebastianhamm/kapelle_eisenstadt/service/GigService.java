package at.sebastianhamm.kapelle_eisenstadt.service;

import at.sebastianhamm.kapelle_eisenstadt.dto.GigDto;

import java.util.List;

public interface GigService {
    List<GigDto> findAll();
    GigDto findById(Long id);
    GigDto save(GigDto gigDto);
    GigDto update(GigDto gigDto);
    void delete(Long id);
}
