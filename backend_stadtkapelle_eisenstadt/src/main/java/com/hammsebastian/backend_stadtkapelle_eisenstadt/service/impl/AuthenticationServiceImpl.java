package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.UserEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.enums.RoleEnum;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.exception.DuplicateEmailException;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.RegisterRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.RegisterResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.UserRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateEmailException("Email is already registered");
        }

        UserEntity newUser = new UserEntity();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole(RoleEnum.USER);
        newUser.setEmailNotification(registerRequest.isEmailNotification());

        UserEntity savedUser = userRepository.save(newUser);

        return RegisterResponse.builder()
                .email(savedUser.getEmail())
                .message("User registered successfully")
                .build();
    }
}
