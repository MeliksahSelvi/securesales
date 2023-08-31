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
@Table(name = "USER_EVENT")
public class UserEvent extends BaseEntity {

    @SequenceGenerator(name = "UserEvent", sequenceName = "USER_EVENT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "UserEvent")
    @Column
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_USER", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_EVENT_USER"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_EVENT", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_EVENT_EVENT"))
    private Event event;

    @Column(length = 100)
    private String device;

    @Column(length = 100)
    private String ipAddress;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
