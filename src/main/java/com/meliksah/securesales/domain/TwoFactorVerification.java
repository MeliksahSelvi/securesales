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
@Table(name = "TWO_FACTOR_VERIFICATION")
public class TwoFactorVerification extends BaseEntity {

    @SequenceGenerator(name = "TwoFactorVerification", sequenceName = "TWO_FACTOR_VERIFCATION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "TwoFactorVerification")
    @Column
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_USER", nullable = false, unique = true, foreignKey = @ForeignKey(name = "FK_TWO_FCTOR_VERIFICATION_USER"))
    private User user;

    @Column(length = 10, unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date expirationDate;
}
