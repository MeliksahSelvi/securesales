package com.meliksah.securesales.resource;

import com.meliksah.securesales.domain.User;
import com.meliksah.securesales.dto.*;
import com.meliksah.securesales.enumeration.RoleType;
import com.meliksah.securesales.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @Author mselvi
 * @Created 10.08.2023
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserResource {

    private final UserService userService;

    @GetMapping("/profile")
    public ApiResponse profile(Authentication authentication) {
        Map<String, Object> data = userService.getUserDataByEmail(authentication);

        return ApiResponse.of(HttpStatus.OK, data, "Profile Retrieved");
    }

    @PutMapping("/update")
    public ApiResponse updateUser(@RequestBody @Valid UpdateUserForm updateUserForm) {
        Map<String, Object> data = userService.updateUserDetails(updateUserForm);

        return ApiResponse.of(HttpStatus.OK, data, "User Updated");
    }

    @GetMapping("/refresh/token")
    public ApiResponse refreshToken(HttpServletRequest request) {
        Map<String, Object> data = userService.getUserToken(request);

        return ApiResponse.of(HttpStatus.OK, data, "Token Refresh");
    }

    @GetMapping("/error")
    public ApiResponse error(HttpServletRequest request) {

        return ApiResponse.of(HttpStatus.BAD_REQUEST, "There is no mapping for a " + request.getMethod() + " request for this path on the server");
    }

    @GetMapping("/resetpassword/{email}")
    public ApiResponse resetPassword(@PathVariable String email) {
        userService.resetPassword(email);

        return ApiResponse.of(HttpStatus.OK, "Email sent. Please check your email to reset your password");
    }

    @PatchMapping("/new/password")
    public ApiResponse createNewPassword(@RequestBody @Valid ResetPasswordForm resetPasswordForm) {
        userService.createNewPassword(resetPasswordForm);

        return ApiResponse.of(HttpStatus.OK, "Password Reset Successfully");
    }

    @PatchMapping("/update/password")
    public ApiResponse updatePassword(Authentication authentication, @RequestBody @Valid UpdatePasswordForm updatePasswordForm) {
        Map<String, Object> data = userService.updatePassword(authentication, updatePasswordForm);

        return ApiResponse.of(HttpStatus.OK, data, "Password Updated Successfully");
    }

    @PatchMapping("/update/role/{roleType}")
    public ApiResponse updateRole(Authentication authentication, @PathVariable RoleType roleType) {
        Map<String, Object> data = userService.updateUserRole(authentication, roleType);

        return ApiResponse.of(HttpStatus.OK, data, "Role Updated Successfully");
    }

    @PatchMapping("/update/settings")
    public ApiResponse updateSettings(Authentication authentication, @RequestBody @Valid SettingsForm settingsForm) {
        Map<String, Object> data = userService.updateAccountSettings(authentication, settingsForm);

        return ApiResponse.of(HttpStatus.OK, data, "Account Settings Updated Successfully");
    }

    @PatchMapping("/update/image")
    public ApiResponse updateImage(Authentication authentication, @RequestParam MultipartFile image) {
        Map<String, Object> data = userService.updateImage(authentication, image);

        return ApiResponse.of(HttpStatus.OK, data, "Profile Image Updated");
    }

    @GetMapping(value = "/image/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getProfileImage(@PathVariable String fileName) throws IOException {

        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/images/" + fileName));
    }

    @PatchMapping("/toggleMfa")
    public ApiResponse toggleMfa(Authentication authentication) {
        Map<String, Object> data = userService.toggleMfa(authentication);

        return ApiResponse.of(HttpStatus.OK, data, "Multi Factor Authenticated Updated");
    }

    @PostMapping("/verify/code")
    public ApiResponse verifyCode(@RequestBody @Valid VerifyCodeForm verifyCodeForm) {
        Map<String, Object> data = userService.verifyCode(verifyCodeForm);

        return ApiResponse.of(HttpStatus.OK, data, "Login Success");
    }

    //verify password
    @GetMapping("/verify/password/{key}")
    public ApiResponse verifyPasswordKey(@PathVariable String key) {
        userService.verifyPasswordKey(key);
        return ApiResponse.of(HttpStatus.OK, "Please enter a new password");
    }

    //verify account
    @GetMapping("/verify/account/{key}")
    public ApiResponse verifyAccountUrl(@PathVariable String key) {
        userService.verifyAccountKey(key);
        return ApiResponse.of(HttpStatus.OK, "Account Verified");
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Valid LoginForm loginForm) {
        Map<String, Object> data = userService.loginUser(loginForm);

        String message = getMessage(data);
        return ApiResponse.of(HttpStatus.OK, data, message);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> saveUser(@RequestBody User user) {
        ApiResponse ApiResponse = userService.createUser(user);

        URI uri = getUri();

        return ResponseEntity.created(uri).body(ApiResponse);
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }

    private String getMessage(Map<String, Object> data) {
        String message = "Login Success";
        UserDTO user = (UserDTO) data.get("user");
        if (user.isUsingMfa()) {
            message = "Verification Code Sent";
        }
        return message;
    }
}
