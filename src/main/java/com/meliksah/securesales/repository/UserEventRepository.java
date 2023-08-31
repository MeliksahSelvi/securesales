package com.meliksah.securesales.repository;

import com.meliksah.securesales.common.BaseRepository;
import com.meliksah.securesales.domain.UserEvent;
import com.meliksah.securesales.dto.UserEventDto;
import com.meliksah.securesales.utils.Queries;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author mselvi
 * @Created 23.08.2023
 */

@Repository
public class UserEventRepository extends BaseRepository<UserEvent, Long> {

    public UserEventRepository() {
        super(UserEvent.class);
    }

    public List<UserEventDto> findAllEventByUserId(Long userId) {
        Query<UserEventDto> query = getCurrentSession().createQuery(Queries.EVENT_DTO_BY_USER_ID, UserEventDto.class);
        query.setParameter("userId", userId);

        query.setResultTransformer(Transformers.aliasToBean(UserEventDto.class));


        return query.getResultList();
    }

}
