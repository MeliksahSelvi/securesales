package com.meliksah.securesales.service;

import com.meliksah.securesales.domain.Role;
import com.meliksah.securesales.domain.User;
import com.meliksah.securesales.domain.UserRole;
import com.meliksah.securesales.enumeration.ErrorMessage;
import com.meliksah.securesales.enumeration.RoleType;
import com.meliksah.securesales.exception.NotFoundException;
import com.meliksah.securesales.repository.RoleRepository;
import com.meliksah.securesales.repository.UserRepository;
import com.meliksah.securesales.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Author mselvi
 * @Created 10.08.2023
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public void addRoleToUser(Long userId, RoleType roleType) {

        log.info("Adding role {} to user id: {}", roleType, userId);

        Role roleByRoleType = checkRoleIsExistByRoleType(roleType);

        User userByid = checkUserIsExistById(userId);

        saveRoleToUser(roleByRoleType, userByid);
    }


    private Role checkRoleIsExistByRoleType(RoleType roleType) {
        Optional<Role> roleOptional = roleRepository.getRoleByRoleType(roleType);

        return roleOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_ROLE_FOUND_BY_NAME);
        });
    }

    private User checkUserIsExistById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        return userOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_USER_FOUND_BY_ID);
        });
    }

    private void saveRoleToUser(Role roleByRoleType, User userByid) {
        UserRole userRole = new UserRole();
        userRole.setUser(userByid);
        userRole.setRole(roleByRoleType);
        userRoleRepository.save(userRole);
    }

    @Transactional
    public void updateUserRoleType(Long userId, RoleType roleType) {

        Role role = checkRoleIsExistByRoleType(roleType);

        UserRole userRoleByUserId = getUserRoleByUserId(userId);

        updateUserRole(role, userRoleByUserId);
    }

    private void updateUserRole(Role role, UserRole userRoleByUserId) {
        userRoleByUserId.setRole(role);
        userRoleRepository.save(userRoleByUserId);
    }

    private UserRole getUserRoleByUserId(Long userId) {
        Optional<UserRole> userRoleOptional = userRoleRepository.findByUserId(userId);

        return userRoleOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_USER_ROLE_BY_USER_ID);
        });
    }

    public Role getRoleByUserId(Long userId) {
        Optional<Role> roleOptional = roleRepository.getRoleByUserId(userId);

        return roleOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_ROLE_FOUND_BY_USER_ID);
        });
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
