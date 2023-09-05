package com.meliksah.securesales.domain;

import com.meliksah.securesales.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

/**
 * @Author mselvi
 * @Created 08.08.2023
 */

@Entity
@Getter
@Setter
@Table(name = "ACCOUNT_VERIFICATION")
public class AccountVerification extends BaseEntity {

    @SequenceGenerator(name = "AccountVerification",sequenceName = "ACCOUNT_VERIFICATION_ID_SEQ",allocationSize = 1)
    @GeneratedValue(generator = "AccountVerification")
    @Column
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_USER",nullable = false, unique = true,foreignKey = @ForeignKey(name = "FK_ACCOUNT_VERIFICATION_USER"))
    private User user;

    @Column(unique = true,nullable = false)
    private String url;
}
