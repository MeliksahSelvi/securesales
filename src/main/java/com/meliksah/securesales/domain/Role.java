package com.meliksah.securesales.domain;

import com.meliksah.securesales.common.BaseEntity;
import com.meliksah.securesales.enumeration.RoleType;
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
@Table(name = "ROLE")
public class Role extends BaseEntity {

    @SequenceGenerator(name = "Role",sequenceName = "ROLE_ID_SEQ",allocationSize = 1)
    @GeneratedValue(generator = "Role")
    @Column
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20,nullable = false,unique = true)
    private RoleType roleType;

    @Column(nullable = false)
    private String permission;

}
