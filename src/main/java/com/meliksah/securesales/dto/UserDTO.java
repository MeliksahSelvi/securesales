package com.meliksah.securesales.dto;

import com.meliksah.securesales.enumeration.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @Author mselvi
 * @Created 10.08.2023
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String title;
    private String bio;
    private boolean enabled;
    private boolean notLocked;
    private boolean usingMfa;
    private Date createdAt;
    private String imageUrl;
    private RoleType roleType;
    private String permissions;
}
