package com.meliksah.securesales.repository;

import com.meliksah.securesales.common.BaseRepository;
import com.meliksah.securesales.domain.Event;
import com.meliksah.securesales.enumeration.EventType;
import com.meliksah.securesales.utils.Queries;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 23.08.2023
 */

@Repository
public class EventRepository extends BaseRepository<Event, Long> {

    public EventRepository() {
        super(Event.class);
    }

    public Optional<Event> findByEventType(EventType eventType) {
        Query<Event> query = getCurrentSession().createQuery(Queries.EVENT_BY_EVENT_TYPE, domainClass);

        query.setParameter("eventType", eventType);

        return query.uniqueResultOptional();
    }
}
