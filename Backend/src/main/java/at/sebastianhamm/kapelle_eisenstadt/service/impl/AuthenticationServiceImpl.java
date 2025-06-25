package at.sebastianhamm.kapelle_eisenstadt.service.impl;

import at.sebastianhamm.kapelle_eisenstadt.config.JwtService;
import at.sebastianhamm.kapelle_eisenstadt.dto.AuthenticationRequest;
import at.sebastianhamm.kapelle_eisenstadt.dto.AuthenticationResponse;
import at.sebastianhamm.kapelle_eisenstadt.dto.RegisterRequest;
import at.sebastianhamm.kapelle_eisenstadt.entity.Role;
import at.sebastianhamm.kapelle_eisenstadt.entity.User;
import at.sebastianhamm.kapelle_eisenstadt.repository.UserRepository;
import at.sebastianhamm.kapelle_eisenstadt.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        // Ensure the default role is set if not provided
        Role role = request.getRole() != null ? request.getRole() : Role.USER;
        
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        
        userRepository.save(user);
        
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
                
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}
