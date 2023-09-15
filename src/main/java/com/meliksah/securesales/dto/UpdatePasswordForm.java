package com.meliksah.securesales.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author mselvi
 * @Created 22.08.2023
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordForm {

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
    private String email;

    @NotEmpty(message = "Current Password cannot be empty")
    private String currentPassword;

    @NotEmpty(message = "New Password cannot be empty")
    private String newPassword;

    @NotEmpty(message = "Confirm Password cannot be empty")
    private String confirmNewPassword;
}
