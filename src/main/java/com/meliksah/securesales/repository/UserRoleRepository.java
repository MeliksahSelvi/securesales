package com.meliksah.securesales.repository;

import com.meliksah.securesales.common.BaseRepository;
import com.meliksah.securesales.domain.UserRole;
import com.meliksah.securesales.utils.Queries;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 10.08.2023
 */

@Repository
public class UserRoleRepository extends BaseRepository<UserRole, Long> {

    public UserRoleRepository() {
        super(UserRole.class);
    }

    public Optional<UserRole> findByUserId(Long userId) {
        Query<UserRole> query = getCurrentSession().createQuery(Queries.USER_ROLE_BY_USER_UD, domainClass);
        query.setParameter("userId", userId);

        return query.uniqueResultOptional();
    }
}
