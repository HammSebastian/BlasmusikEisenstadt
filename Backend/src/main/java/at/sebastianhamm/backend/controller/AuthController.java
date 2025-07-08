package at.sebastianhamm.backend.controller;

import java.util.*;
import java.util.stream.Collectors;

import at.sebastianhamm.backend.jwt.JwtUtils;
import at.sebastianhamm.backend.models.*;
import at.sebastianhamm.backend.payload.request.LoginRequest;
import at.sebastianhamm.backend.payload.request.SignupRequest;
import at.sebastianhamm.backend.payload.response.MessageResponse;
import at.sebastianhamm.backend.payload.response.UserInfoResponse;
import at.sebastianhamm.backend.repository.RoleRepository;
import at.sebastianhamm.backend.repository.UserRepository;
import at.sebastianhamm.backend.services.EmailService;
import at.sebastianhamm.backend.services.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "The authentication endpoint")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles,
                        jwtUtils.getRefreshToken(),
                        jwtCookie.getValue()
                ));
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_MUSICIAN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "section_leader":
                        Role sectionLeaderRole = roleRepository.findByName(ERole.ROLE_SECTION_LEADER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(sectionLeaderRole);

                        break;
                    case "conductor":
                        Role conductorRole = roleRepository.findByName(ERole.ROLE_CONDUCTOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(conductorRole);

                        break;
                    case "reporter":
                        Role reporterRole = roleRepository.findByName(ERole.ROLE_REPORTER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(reporterRole);

                        break;

                    default:
                        Role musicianRole = roleRepository.findByName(ERole.ROLE_MUSICIAN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(musicianRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("username", user.getUsername());
        Mail mail = Mail.builder()
                .from("no-reply@stadtkapelle-eisenstadt.at")
                .to(user.getEmail())
                .htmlTemplate(new HtmlTemplate("welcome-email", properties))
                .subject("Willkommen bei Stadtkapelle Eisenstadt")
                .build();
        emailService.sendWelcomeEmail(mail);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
