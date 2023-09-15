package com.meliksah.securesales.utils;

import com.meliksah.securesales.domain.User;
import com.meliksah.securesales.dto.UserDTO;
import com.meliksah.securesales.mapper.UserDTOMapper;
import org.springframework.security.core.Authentication;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 21.08.2023
 */

public class UserUtil {

    public static UserDTO getAuthenticatedUserDTO(Authentication authentication) {
        Optional<User> userOptional = (Optional<User>) authentication.getPrincipal();
        return UserDTOMapper.fromUser(userOptional.get());
    }

    public static User getAuthenticatedUser(Authentication authentication) {
        Optional<User> userOptional = (Optional<User>) authentication.getPrincipal();
        return userOptional.get();
    }

}
