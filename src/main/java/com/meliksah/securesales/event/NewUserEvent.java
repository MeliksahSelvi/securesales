package com.meliksah.securesales.event;

import com.meliksah.securesales.enumeration.EventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @Author mselvi
 * @Created 23.08.2023
 */

@Getter
@Setter
public class NewUserEvent extends ApplicationEvent {

    private String email;
    private EventType eventType;

    public NewUserEvent(String email, EventType eventType) {
        super(email);
        this.email = email;
        this.eventType = eventType;
    }
}
