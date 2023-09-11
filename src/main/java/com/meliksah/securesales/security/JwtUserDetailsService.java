package com.meliksah.securesales.security;

import com.meliksah.securesales.domain.Role;
import com.meliksah.securesales.domain.User;
import com.meliksah.securesales.repository.RoleRepository;
import com.meliksah.securesales.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 10.08.2023
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userByEmail = userRepository.getUserByEmail(email);

        if (userByEmail.isEmpty()) {
            throw new UsernameNotFoundException("User not found in the database");
        }

        log.info("User found in the database: {}",email);
        User user = userByEmail.get();
        Optional<Role> roleByUserId = roleRepository.getRoleByUserId(user.getId());
        return new UserPrincipal(user, roleByUserId.get());
    }
}
