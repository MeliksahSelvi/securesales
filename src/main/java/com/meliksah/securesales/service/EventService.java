package com.meliksah.securesales.service;

import com.meliksah.securesales.domain.Event;
import com.meliksah.securesales.domain.User;
import com.meliksah.securesales.domain.UserEvent;
import com.meliksah.securesales.dto.UserEventDto;
import com.meliksah.securesales.enumeration.ErrorMessage;
import com.meliksah.securesales.enumeration.EventType;
import com.meliksah.securesales.exception.NotFoundException;
import com.meliksah.securesales.repository.EventRepository;
import com.meliksah.securesales.repository.UserEventRepository;
import com.meliksah.securesales.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author mselvi
 * @Created 23.08.2023
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final UserEventRepository userEventRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {

        User user = findUserByEmail(email);
        Event event = findEventByType(eventType);

        saveUserEvent(device, ipAddress, user, event);
    }

    private Event findEventByType(EventType eventType) {
        Optional<Event> eventOptional = eventRepository.findByEventType(eventType);

        return eventOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_EVENT_FOUND_BY_EVENT_TYPE);
        });
    }

    private User findUserByEmail(String email){
        Optional<User> userOptional = userRepository.getUserByEmail(email);

        return userOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_USER_FOUND_BY_EMAIL);
        });
    }

    private void saveUserEvent(String device, String ipAddress, User user, Event event) {
        UserEvent userEvent=new UserEvent();
        userEvent.setUser(user);
        userEvent.setEvent(event);
        userEvent.setDevice(device);
        userEvent.setIpAddress(ipAddress);
        userEvent.setCreatedAt(new Date());
        userEventRepository.save(userEvent);
    }

    public void addUserEvent(Long userId, EventType eventType, String device, String ipAddress) {

        User user = findUserById(userId);
        Event event = findEventByType(eventType);

        saveUserEvent(device, ipAddress, user, event);
    }

    private User findUserById(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);

        return userOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_USER_FOUND_BY_EMAIL);
        });
    }

    public List<UserEventDto> findUserEventsByUserId(Long userId){
        return userEventRepository.findAllEventByUserId(userId);
    }
}
