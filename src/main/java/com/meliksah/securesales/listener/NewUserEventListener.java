package com.meliksah.securesales.listener;

import com.meliksah.securesales.event.NewUserEvent;
import com.meliksah.securesales.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.meliksah.securesales.utils.RequestUtils.getDevice;
import static com.meliksah.securesales.utils.RequestUtils.getIpAddress;

/**
 * @Author mselvi
 * @Created 23.08.2023
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class NewUserEventListener {

    private final EventService eventService;
    private final HttpServletRequest request;

    @EventListener
    public void onNewUserEvent(NewUserEvent event) {
        log.info("New User Event is fired");
        eventService.addUserEvent(event.getEmail(), event.getEventType(), getDevice(request), getIpAddress(request));
    }
}
