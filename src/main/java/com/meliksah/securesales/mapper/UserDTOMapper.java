package com.meliksah.securesales.mapper;

import com.meliksah.securesales.domain.Role;
import com.meliksah.securesales.domain.User;
import com.meliksah.securesales.dto.UpdateUserForm;
import com.meliksah.securesales.dto.UserDTO;
import org.springframework.beans.BeanUtils;

/**
 * @Author mselvi
 * @Created 10.08.2023
 */

public class UserDTOMapper {

    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public static UserDTO fromUser(User user, Role role) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoleType(role.getRoleType());
        userDTO.setPermissions(role.getPermission());
        return userDTO;
    }

    public static User fromDTO(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }

    public static User copyUpdateFormToUser(User user, UpdateUserForm updateUserForm) {
        BeanUtils.copyProperties(updateUserForm, user);
        return user;
    }
}
