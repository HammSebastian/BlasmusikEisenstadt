package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.UserEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));

        // Convert the user's role to a list of authorities
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + existingUser.getRole().name())
        );
        
        return new User(
                existingUser.getEmail(),
                existingUser.getPassword(),
                authorities
        );
    }
}
