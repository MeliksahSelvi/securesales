package com.meliksah.securesales.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.meliksah.securesales.domain.ResetPasswordVerification;
import com.meliksah.securesales.domain.Role;
import com.meliksah.securesales.domain.TwoFactorVerification;
import com.meliksah.securesales.domain.User;
import com.meliksah.securesales.dto.*;
import com.meliksah.securesales.enumeration.ErrorMessage;
import com.meliksah.securesales.enumeration.EventType;
import com.meliksah.securesales.enumeration.RoleType;
import com.meliksah.securesales.enumeration.VerificationType;
import com.meliksah.securesales.event.NewUserEvent;
import com.meliksah.securesales.exception.*;
import com.meliksah.securesales.mapper.UserDTOMapper;
import com.meliksah.securesales.repository.UserRepository;
import com.meliksah.securesales.security.TokenProvider;
import com.meliksah.securesales.security.UserPrincipal;
import com.meliksah.securesales.utils.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.meliksah.securesales.constants.Constants.TOKEN_PREFIX;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @Author mselvi
 * @Created 10.08.2023
 */

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final VerificationService verificationService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder encoder;
    private final EventService eventService;
    private final ApplicationEventPublisher publisher;




    public ApiResponse createUser(User user) {
        boolean emailAlreadyUse = checkEmailAlreadyUse(user.getEmail());

        if (!emailAlreadyUse) {
            user = saveUser(user);

            roleService.addRoleToUser(user.getId(), RoleType.ROLE_USER);

            String verificationUrl = getAccountVerificationUrl(VerificationType.ACCOUNT);

            verificationService.saveAccountVerification(user, verificationUrl);

            sendVerificationEmail(user, verificationUrl, VerificationType.ACCOUNT);

            UserDTO userDTO = getUserDTO(user);

            ApiResponse httpResponse = createUserResponse(userDTO);

            return httpResponse;
        } else {
            throw new ApiException(ErrorMessage.EMAIL_ALREADY_IN_USE);
        }
    }

    private void sendVerificationEmail(User user, String verificationUrl, VerificationType account) {
        CompletableFuture.runAsync(() -> emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationUrl, account));
    }

    private boolean checkEmailAlreadyUse(String email) {

        Optional<User> optional = userRepository.getUserByEmail(email);

        return optional.isPresent();
    }

    private User saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setNotLocked(true);
        user.setEnabled(false);
        user.setUsingMfa(false);
        user.setCreatedAt(new Date());

        user = userRepository.save(user);
        return user;
    }

    private String getAccountVerificationUrl(VerificationType verificationType) {
        String key = UUID.randomUUID().toString();
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + verificationType + "/" + key).toUriString();
    }

    private ApiResponse createUserResponse(UserDTO userDTO) {
        ApiResponse httpResponse = ApiResponse.of(HttpStatus.CREATED, Map.of("user", userDTO), String.format("User account created for user %s", userDTO.getFirstName()));
        return httpResponse;
    }

    public Map<String, Object> loginUser(LoginForm loginForm) {

        Authentication authentication = authenticateUser(loginForm.getEmail(), loginForm.getPassword());

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User user = userPrincipal.getUser();

        UserDTO userDTO = getUserDTO(user);

        sendVerificationCodeByMfa(user);

        Map<String, Object> dataMap = getLoginResponseData(userPrincipal, user, userDTO);

        return dataMap;
    }

    private void sendVerificationCodeByMfa(User user) {
        if (user.isUsingMfa()) {
            verificationService.sendVerificationCode(user);
        }
    }

    private Map<String, Object> getLoginResponseData(UserPrincipal userPrincipal, User user, UserDTO userDTO) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", userDTO);
        if (!user.isUsingMfa()) {
            String accessToken = tokenProvider.createAccessToken(userPrincipal);
            String refreshToken = tokenProvider.createRefreshToken(userPrincipal);
            dataMap.put("access_token", accessToken);
            dataMap.put("refresh_token", refreshToken);
        }
        return dataMap;
    }

    private Authentication authenticateUser(String email, String password) {
        User userByEmail = getUserByEmail(email);
        try {
            if (userByEmail != null) {
                createEventByType(userByEmail.getEmail(), EventType.LOGIN_ATTEMPT);
            }
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            if (!userByEmail.isUsingMfa()) {
                createEventByType(userByEmail.getEmail(), EventType.LOGIN_ATTEMPT_SUCCESS);
            }

            return authentication;
        } catch (LockedException e) {
            createEventByType(email, EventType.LOGIN_ATTEMPT_FAILURE);
            throw new LockedException(e.getMessage());
        } catch (DisabledException e) {
            createEventByType(email, EventType.LOGIN_ATTEMPT_FAILURE);
            throw new DisabledException(e.getMessage());
        } catch (JWTVerificationException e) {
            createEventByType(email, EventType.LOGIN_ATTEMPT_FAILURE);
            throw new JWTVerificationException(e.getMessage());
        }
    }

    private void createEventByType(String email, EventType loginAttemptFailure) {
        publisher.publishEvent(new NewUserEvent(email, loginAttemptFailure));
    }

    public Map<String, Object> verifyCode(VerifyCodeForm verifyCodeForm) {
        User userByCode = checkVerifyCode(verifyCodeForm);

        verificationService.deleteExVerificationCode(verifyCodeForm.getCode());

        UserDTO userDTO = getUserDTO(userByCode);

        UserPrincipal userPrincipal = getUserPrincipal(userByCode);

        Map<String, Object> responseData = createResponseData(userDTO, userPrincipal);

        return responseData;
    }

    private User checkVerifyCode(VerifyCodeForm verifyCodeForm) {
        validateCodeIsExpired(verifyCodeForm.getCode());

        User user = validateCodeIsInvalid(verifyCodeForm);

        return user;
    }

    private void validateCodeIsExpired(String code) {
        TwoFactorVerification twoFactorVerificationByCode = verificationService.findTwoFactorVerificationByCode(code);
        Date expirationDate = twoFactorVerificationByCode.getExpirationDate();

        if (expirationDate.before(new Date())) {
            throw new CodeException(ErrorMessage.CODE_IS_EXPIRED);
        }
    }

    private User validateCodeIsInvalid(VerifyCodeForm verifyCodeForm) {
        Optional<User> userOptional = userRepository.getUserByEmailAndTwoFactorCode(verifyCodeForm);

        return userOptional.orElseThrow(() -> {
            throw new CodeException(ErrorMessage.CODE_IS_INVALID);
        });
    }

    private Map<String, Object> createResponseData(UserDTO userDTO, UserPrincipal userPrincipal) {
        String accessToken = tokenProvider.createAccessToken(userPrincipal);
        String refreshToken = tokenProvider.createRefreshToken(userPrincipal);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", userDTO);
        dataMap.put("access_token", accessToken);
        dataMap.put("refresh_token", refreshToken);
        return dataMap;
    }

    private UserDTO getUserDTO(User user) {
        Role roleByUserId = roleService.getRoleByUserId(user.getId());

        UserDTO userDTO = UserDTOMapper.fromUser(user, roleByUserId);
        return userDTO;
    }

    private UserPrincipal getUserPrincipal(User user) {
        return new UserPrincipal(user, roleService.getRoleByUserId(user.getId()));
    }

    private User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_USER_FOUND_BY_EMAIL);
        });
    }

    public Map<String, Object> getUserDataByEmail(Authentication authentication) {

        UserDTO authenticatedUser = UserUtil.getAuthenticatedUserDTO(authentication);

        Map<String, Object> dataMap = createProfileData(authenticatedUser);
        return dataMap;
    }

    private Map<String, Object> createProfileData(UserDTO authenticatedUser) {
        List<UserEventDto> userEventDtoList = eventService.findUserEventsByUserId(authenticatedUser.getId());
        List<Role> roleList = roleService.getRoles();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", authenticatedUser);
        dataMap.put("roles", roleList);
        dataMap.put("events", userEventDtoList);
        return dataMap;
    }

    public void resetPassword(String email) {

        User user = getUserByEmail(email);

        String verificationUrl = getAccountVerificationUrl(VerificationType.PASSWORD);

        verificationService.resetPasswordVerification(user, verificationUrl);

        sendVerificationEmail(user, verificationUrl, VerificationType.PASSWORD);
    }

    public void verifyPasswordKey(String url) {
        validatePasswordUrlIsExpired(url);

        validatePasswordUrlIsInvalid(url);
    }

    private void validatePasswordUrlIsExpired(String url) {
        ResetPasswordVerification resetPasswordVerification = verificationService.findPasswordVerificationByUrl(url);
        Date expirationDate = resetPasswordVerification.getExpirationDate();

        if (expirationDate.before(new Date())) {
            throw new UrlException(ErrorMessage.URL_IS_EXPIRED);
        }
    }

    private void validatePasswordUrlIsInvalid(String url) {
        Optional<User> userOptional = userRepository.getUserByPasswordUrl(url);

        userOptional.orElseThrow(() -> {
            throw new UrlException(ErrorMessage.URL_IS_INVALID);
        });
    }

    public void createNewPassword(ResetPasswordForm resetPasswordForm) {

        validatePasswordsIsSame(resetPasswordForm.getPassword(), resetPasswordForm.getConfirmPassword());

        User user = getUser(resetPasswordForm.getUserId());

        updateUserPassword(user, resetPasswordForm.getPassword());

        verificationService.deleteExPasswordUrlByUserId(resetPasswordForm.getUserId());
    }

    private void updateUserPassword(User userByUrl, String password) {
        userByUrl.setPassword(encoder.encode(password));
        userRepository.save(userByUrl);
    }

    private void validatePasswordsIsSame(String password, String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            throw new PasswordException(ErrorMessage.PASSWORDS_IS_NOT_SAME);
        }
    }

    public void verifyAccountKey(String key) {
        User user = checkUserByKey(key);

        updateUserEnabled(user);

        verificationService.deleteExAccountVerificationUrl(key);
    }

    private void updateUserEnabled(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    private User checkUserByKey(String url) {

        Optional<User> userOptional = userRepository.getUserByAccountUrl(url);

        User user = userOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.URL_IS_INVALID);
        });

        if (user.isEnabled()) {
            throw new UrlException(ErrorMessage.URL_IS_ALREADY_VERIFIED);
        }

        return user;
    }

    public Map<String, Object> getUserToken(HttpServletRequest request) {

        checkRequest(request);

        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(TOKEN_PREFIX.length());
        String email = tokenProvider.getSubject(refreshToken, request);
        User userByEmail = getUserByEmail(email);

        UserDTO userDTO = getUserDTO(userByEmail);

        UserPrincipal userPrincipal = getUserPrincipal(userByEmail);

        Map<String, Object> refreshTokenData = createRefreshTokenData(userDTO, userPrincipal, refreshToken);
        return refreshTokenData;
    }


    private void checkRequest(HttpServletRequest request) {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            throw new TokenException(ErrorMessage.REQUEST_HAS_NOT_TOKEN);
        }

        String token = header.substring(TOKEN_PREFIX.length());
        String email = tokenProvider.getSubject(token, request);

        if (!tokenProvider.isTokenValid(email, token)) {
            throw new TokenException(ErrorMessage.TOKEN_IS_INVALID);
        }
    }

    private Map<String, Object> createRefreshTokenData(UserDTO userDTO, UserPrincipal userPrincipal, String refreshToken) {
        String accessToken = tokenProvider.createAccessToken(userPrincipal);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", userDTO);
        dataMap.put("access_token", accessToken);
        dataMap.put("refresh_token", refreshToken);
        return dataMap;
    }

    public Map<String, Object> updateUserDetails(UpdateUserForm updateUserForm) {
        User user = getUser(updateUserForm.getId());

        user = updateUser(updateUserForm, user);

        createEventByType(user.getEmail(), EventType.PROFILE_UPDATE);

        UserDTO userDTO = getUserDTO(user);

        Map<String, Object> data = createProfileData(userDTO);

        return data;
    }

    private User updateUser(UpdateUserForm updateUserForm, User user) {
        user = UserDTOMapper.copyUpdateFormToUser(user, updateUserForm);
        return userRepository.save(user);
    }

    private User getUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_USER_FOUND_BY_ID);
        });
    }

    public Map<String, Object> updatePassword(Authentication authentication, UpdatePasswordForm updatePasswordForm) {

        validatePasswordsIsSame(updatePasswordForm.getNewPassword(), updatePasswordForm.getConfirmNewPassword());

        UserDTO authenticatedUser = UserUtil.getAuthenticatedUserDTO(authentication);

        User user = getUser(authenticatedUser.getId());

        validateCurrentPassword(updatePasswordForm.getCurrentPassword(), user.getPassword());

        updateUserPassword(user, updatePasswordForm.getNewPassword());

        createEventByType(user.getEmail(), EventType.PASSWORD_UPDATE);

        UserDTO userDTO = UserDTOMapper.fromUser(user);

        Map<String, Object> data = createProfileData(userDTO);

        return data;
    }

    private void validateCurrentPassword(String currentPassword, String password) {
        if (!encoder.matches(currentPassword, password)) {
            throw new PasswordException(ErrorMessage.WRONG_CURRENT_PASSWORD);
        }
    }

    public Map<String, Object> updateUserRole(Authentication authentication, RoleType roleType) {
        UserDTO authenticatedUser = UserUtil.getAuthenticatedUserDTO(authentication);

        roleService.updateUserRoleType(authenticatedUser.getId(), roleType);

        createEventByType(authenticatedUser.getEmail(), EventType.ROLE_UPDATE);

        Map<String, Object> data = createProfileData(authenticatedUser);

        return data;
    }

    private Map<String, Object> createUpdateRoleData(UserDTO userDTO) {

        List<Role> roleList = roleService.getRoles();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", userDTO);
        dataMap.put("roles", roleList);

        return dataMap;
    }

    public Map<String, Object> updateAccountSettings(Authentication authentication, SettingsForm settingsForm) {

        UserDTO authenticatedUser = UserUtil.getAuthenticatedUserDTO(authentication);

        User user = updateUserAccountSettings(settingsForm, authenticatedUser);

        createEventByType(authenticatedUser.getEmail(), EventType.ACCOUNT_SETTINGS_UPDATE);

        UserDTO userDTO = UserDTOMapper.fromUser(user);

        Map<String, Object> data = createUpdateRoleData(userDTO);

        return data;
    }

    private User updateUserAccountSettings(SettingsForm settingsForm, UserDTO authenticatedUser) {
        User user = getUser(authenticatedUser.getId());
        user.setEnabled(settingsForm.getEnabled());
        user.setNotLocked(settingsForm.getNotLocked());
        return userRepository.save(user);
    }

    public Map<String, Object> toggleMfa(Authentication authentication) {
        UserDTO authenticatedUser = UserUtil.getAuthenticatedUserDTO(authentication);

        User user = getUserByEmail(authenticatedUser.getEmail());

        checkUserPhone(user.getPhone());

        user = updateUserMultiFactorAuthentication(user);

        createEventByType(user.getEmail(), EventType.MFA_UPDATE);

        UserDTO userDTO = UserDTOMapper.fromUser(user);

        Map<String, Object> data = createProfileData(userDTO);

        return data;
    }

    private User updateUserMultiFactorAuthentication(User user) {
        user.setUsingMfa(!user.isUsingMfa());
        user = userRepository.save(user);
        return user;
    }

    private void checkUserPhone(String phone) {
        if (Strings.isBlank(phone)) {
            throw new NotFoundException(ErrorMessage.NEED_PHONE_NUMBER);
        }
    }

    public Map<String, Object> updateImage(Authentication authentication, MultipartFile image) {

        UserDTO authenticatedUser = UserUtil.getAuthenticatedUserDTO(authentication);

        String email = authenticatedUser.getEmail();

        User user = updateUserImage(email);

        saveImage(email, image);

        createEventByType(email, EventType.PROFILE_PICTURE_UPDATE);

        UserDTO userDTO = UserDTOMapper.fromUser(user);
        Map<String, Object> updateImageData = createProfileData(userDTO);

        return updateImageData;
    }

    private User updateUserImage(String email) {
        User user = getUserByEmail(email);
        String imageUrl = getImageUrl(email);
        user.setImageUrl(imageUrl);
        user = userRepository.save(user);
        return user;
    }

    private String getImageUrl(String email) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/" + email + ".png").toUriString();
    }

    private void saveImage(String email, MultipartFile image) {
        Path fileStorageLocation = Paths.get(System.getProperty("user.home") + "/Downloads/images/").toAbsolutePath().normalize();

        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(fileStorageLocation);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ApiException(ErrorMessage.UNABLE_DIRECTORIES);
            }
            log.info("Created Directories: {}", fileStorageLocation);
        }
        try {
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(email + ".png"), REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ApiException(ErrorMessage.ERROR_IN_COPY_IMAGE);
        }
        log.info("File Saved In: {} folder", fileStorageLocation);
    }

}
