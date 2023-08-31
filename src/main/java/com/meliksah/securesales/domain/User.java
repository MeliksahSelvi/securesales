package com.meliksah.securesales.domain;

import com.meliksah.securesales.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author mselvi
 * @Created 08.08.2023
 */

@Entity
@Getter
@Setter
@Table(name = "USER")
public class User extends BaseEntity {

    @SequenceGenerator(name = "User", sequenceName = "USER_ID_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "User")
    @Column
    @Id
    private Long id;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String address;

    @Column(length = 30)
    private String phone;

    @Column(length = 30)
    private String title;

    @Column
    private String bio;

    /*
     * This user is enabled or disable?
     * */
    @Column(columnDefinition = "boolean default false")
    private boolean enabled;

    /*
     * This user is locked or notlocked?
     * */
    @Column(columnDefinition = "boolean default true")
    private boolean notLocked;

    /*
     * This user is using multi-factor authentication?
     * */
    @Column(columnDefinition = "boolean default false")
    private boolean usingMfa;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(columnDefinition = "varchar(255) default 'e7.pngegg.com/pngimages/987/118/png-clipart-computer-icons-login-user-profile-others-computer-logo-thumbnail.png'")
    private String imageUrl;

}
