package at.sebastianhamm.kapelle_eisenstadt.service;

import at.sebastianhamm.kapelle_eisenstadt.dto.UserRequest;
import at.sebastianhamm.kapelle_eisenstadt.dto.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse findById(Long id);
    UserResponse findByUsername(String username);
    UserResponse save(UserRequest userRequest);
    UserResponse update(Long id, UserRequest userRequest);
    void delete(Long id);
}
