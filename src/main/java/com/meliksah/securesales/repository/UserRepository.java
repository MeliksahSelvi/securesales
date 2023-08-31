package com.meliksah.securesales.repository;

import com.meliksah.securesales.common.BaseRepository;
import com.meliksah.securesales.domain.User;
import com.meliksah.securesales.dto.VerifyCodeForm;
import com.meliksah.securesales.utils.Queries;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 09.08.2023
 */

@Repository
public class UserRepository extends BaseRepository<User, Long> {

    public UserRepository() {
        super(User.class);
    }

    public Optional<User> getUserByEmail(String email) {
        Query<User> query = getCurrentSession().createQuery(Queries.USER_BY_EMAIL, domainClass);
        query.setParameter("email", email);

        Optional<User> optional = query.uniqueResultOptional();
        return optional;
    }

    public Optional<User> getUserByEmailAndTwoFactorCode(VerifyCodeForm verifyCodeForm) {
        Query<User> query = getCurrentSession().createQuery(Queries.USER_BY_EMAIL_AND_TWO_FACTOR_CODE, domainClass);
        query.setParameter("email", verifyCodeForm.getEmail());
        query.setParameter("code", verifyCodeForm.getCode());

        Optional<User> userOptional = query.uniqueResultOptional();
        return userOptional;
    }

    public Optional<User> getUserByPasswordUrl(String url) {
        Query<User> query = getCurrentSession().createQuery(Queries.USER_BY_PASSWORD_URL, domainClass);
        query.setParameter("url", url);

        Optional<User> userOptional = query.uniqueResultOptional();
        return userOptional;
    }

    public Optional<User> getUserByAccountUrl(String url) {
        Query<User> query = getCurrentSession().createQuery(Queries.USER_BY_ACCOUNT_URL, domainClass);
        query.setParameter("url", url);

        Optional<User> userOptional = query.uniqueResultOptional();
        return userOptional;
    }

}
