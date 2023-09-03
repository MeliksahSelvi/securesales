package com.meliksah.securesales.repository;

import com.meliksah.securesales.common.BaseRepository;
import com.meliksah.securesales.domain.Role;
import com.meliksah.securesales.enumeration.RoleType;
import com.meliksah.securesales.utils.Queries;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 09.08.2023
 */

@Repository
public class RoleRepository extends BaseRepository<Role, Long> {

    public RoleRepository() {
        super(Role.class);
    }

    public Optional<Role> getRoleByRoleType(RoleType roleType) {
        Query<Role> query = getCurrentSession().createQuery(Queries.ROLE_BY_ROLETYPE, domainClass);
        query.setParameter("roleType", roleType);

        Optional<Role> roleOptional = query.uniqueResultOptional();
        return roleOptional;
    }

    public Optional<Role> getRoleByUserId(Long userId) {
        Query<Role> query = getCurrentSession().createQuery(Queries.ROLE_BY_USER_ID, domainClass);
        query.setParameter("userId", userId);

        Optional<Role> roleOptional = query.uniqueResultOptional();
        return roleOptional;
    }

    public Optional<Role> getRoleByUserEmail(String email) {
        Query<Role> query = getCurrentSession().createQuery(Queries.ROLE_BY_EMAIL, domainClass);
        query.setParameter("email", email);

        Optional<Role> roleOptional = query.uniqueResultOptional();
        return roleOptional;
    }
}
