package com.meliksah.securesales.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author mselvi
 * @Created 22.08.2023
 */

@Getter
@Setter
public class UpdateUserForm {

    @NotNull(message = "ID cannot be null or empty")
    private Long id;
    @NotEmpty(message = "First mame cannot be empty")
    private String firstName;
    @NotEmpty(message = "Last mame cannot be empty")
    private String lastName;
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
    private String email;
    @Pattern(regexp = "^\\d{11}$",message = "Invalid phone number")
    private String phone;
    private String address;
    private String title;
    private String bio;

}
