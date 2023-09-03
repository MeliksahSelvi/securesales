package com.meliksah.securesales.domain;

import com.meliksah.securesales.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author mselvi
 * @Created 08.08.2023
 */

@Entity
@Getter
@Setter
@Table(name = "USER_ROLE")
public class UserRole extends BaseEntity {

    @SequenceGenerator(name = "UserRole", sequenceName = "USER_ROLE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "UserRole")
    @Column
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_USER", nullable = false, unique = true, foreignKey = @ForeignKey(name = "FK_USER_ROLE_USER"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_ROLE", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_ROLE_ROLE"))
    private Role role;
}
