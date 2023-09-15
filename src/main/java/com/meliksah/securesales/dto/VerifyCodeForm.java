package com.meliksah.securesales.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author mselvi
 * @Created 16.08.2023
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCodeForm {

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
    private String email;
    @NotEmpty(message = "Code cannot be empty")
    private String code;
}
