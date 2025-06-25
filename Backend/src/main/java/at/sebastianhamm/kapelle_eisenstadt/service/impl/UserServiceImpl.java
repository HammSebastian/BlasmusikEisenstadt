package at.sebastianhamm.kapelle_eisenstadt.service.impl;

import at.sebastianhamm.kapelle_eisenstadt.dao.UserDao;
import at.sebastianhamm.kapelle_eisenstadt.dto.UserRequest;
import at.sebastianhamm.kapelle_eisenstadt.dto.UserResponse;
import at.sebastianhamm.kapelle_eisenstadt.entity.User;
import at.sebastianhamm.kapelle_eisenstadt.exception.ResourceAlreadyExistsException;
import at.sebastianhamm.kapelle_eisenstadt.exception.ResourceNotFoundException;
import at.sebastianhamm.kapelle_eisenstadt.service.UserService;
import at.sebastianhamm.kapelle_eisenstadt.util.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponse> findAll() {
        return userDao.findAll().stream()
                .map(DtoMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return DtoMapper.toUserResponse(user);
    }

    @Override
    public UserResponse findByUsername(String username) {
        return userDao.findByUsername(username)
                .map(DtoMapper::toUserResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    @Override
    public UserResponse save(UserRequest userRequest) {
        // Check if username already exists
        if (userDao.existsByUsername(userRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("User", "username", userRequest.getUsername());
        }
        
        User user = DtoMapper.toUser(userRequest);
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User savedUser = userDao.save(user);
        return DtoMapper.toUserResponse(savedUser);
    }

    @Override
    public UserResponse update(Long id, UserRequest userRequest) {
        User existingUser = userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Check if the new username is already taken by another user
        if (!existingUser.getUsername().equals(userRequest.getUsername()) && 
            userDao.existsByUsername(userRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("User", "username", userRequest.getUsername());
        }
        
        existingUser.setUsername(userRequest.getUsername());
        // Only update password if it's provided
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        existingUser.setRole(userRequest.getRole());
        
        User updatedUser = userDao.save(existingUser);
        return DtoMapper.toUserResponse(updatedUser);
    }

    @Override
    public void delete(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userDao.delete(user);
    }
}
