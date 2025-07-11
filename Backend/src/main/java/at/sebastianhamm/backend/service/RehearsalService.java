package at.sebastianhamm.backend.service;


import at.sebastianhamm.backend.payload.request.RehearsalRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.RehearsalResponse;

import java.util.List;
import java.util.Optional;

public interface RehearsalService {

    ApiResponse<RehearsalResponse> createRehearsal(RehearsalRequest request);

    List<RehearsalResponse> getAllRehearsals();

    Optional<RehearsalResponse> getRehearsalById(Long id);

    ApiResponse<RehearsalResponse> updateRehearsal(Long id, RehearsalRequest request);

    boolean deleteRehearsal(Long id);
}
