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
@Table(name = "RESET_PASSWORD_VERIFICATION")
public class ResetPasswordVerification extends BaseEntity {

    @SequenceGenerator(name = "ResetPasswordVerification",sequenceName = "RESET_PSWRD_VERIFCITION_ID_SEQ",allocationSize = 1)
    @GeneratedValue(generator = "ResetPasswordVerification")
    @Column
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_USER",nullable = false, unique = true,foreignKey = @ForeignKey(name = "FK_RST_PSWRD_VERIFICATION_USER"))
    private User user;

    @Column(unique = true,nullable = false)
    private String url;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date expirationDate;
}
