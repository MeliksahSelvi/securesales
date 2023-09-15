package com.meliksah.securesales.dto;

import com.meliksah.securesales.enumeration.EventType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author mselvi
 * @Created 23.08.2023
 */

@Getter
@Setter
public class UserEventDto {

    private Long userEventId;
    private EventType eventType;
    private String description;
    private String device;
    private String ipAddress;
    private Date createdAt;
}
