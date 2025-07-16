package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.UserEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.exception.UserNotFoundException;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ProfileResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.UserRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;


    @Override
    public ProfileResponse getProfile(String email) {
        return mapToProfileResponse(userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found")));
    }

    private ProfileResponse mapToProfileResponse(UserEntity user) {
        return ProfileResponse.builder()
                .email(user.getEmail())
                .role(user.getRole().name())
                .emailNotification(user.isEmailNotification())
                .avatarUrl(user.getAvatarUrl())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
