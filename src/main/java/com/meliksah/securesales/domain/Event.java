package com.meliksah.securesales.domain;

import com.meliksah.securesales.common.BaseEntity;
import com.meliksah.securesales.enumeration.EventType;
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
@Table(name = "EVENT")
public class Event extends BaseEntity {

    @SequenceGenerator(name = "Event",sequenceName = "EVENT_ID_SEQ",allocationSize = 1)
    @GeneratedValue(generator = "Event")
    @Column
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30,nullable = false,unique = true)
    private EventType eventType;

    @Column(nullable = false)
    private String description;

}
