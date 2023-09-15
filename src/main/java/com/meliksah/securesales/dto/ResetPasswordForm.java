package com.meliksah.securesales.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author mselvi
 * @Created 18.08.2023
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordForm {

    @NotEmpty(message = "User ID cannot be empty")
    private Long userId;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "Confirm password cannot be empty")
    private String confirmPassword;
}
