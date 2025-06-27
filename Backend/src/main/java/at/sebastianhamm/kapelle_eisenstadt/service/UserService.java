package at.sebastianhamm.kapelle_eisenstadt.service;

import at.sebastianhamm.kapelle_eisenstadt.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto save(UserDto user);

    List<UserDto> findAll();

    void delete(Long id);

    UserDto findByName(String username);

    UserDto findById(Long id);

    UserDto update(UserDto userDto);
}
