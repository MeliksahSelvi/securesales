package com.meliksah.securesales.repository;

import com.meliksah.securesales.common.BaseRepository;
import com.meliksah.securesales.domain.TwoFactorVerification;
import com.meliksah.securesales.utils.Queries;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 10.08.2023
 */

@Repository
public class TwoFactorVerificationRepository extends BaseRepository<TwoFactorVerification, Long> {
    public TwoFactorVerificationRepository() {
        super(TwoFactorVerification.class);
    }

    public void deleteByUserId(Long userId) {
        Query query = getCurrentSession().createQuery(Queries.DELETE_CODE_BY_USER_ID);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    public void deleteByCode(String code) {
        Query query = getCurrentSession().createQuery(Queries.DELETE_CODE);
        query.setParameter("code", code);
        query.executeUpdate();
    }

    public Optional<TwoFactorVerification> findByCode(String code) {
        Query<TwoFactorVerification> query = getCurrentSession().createQuery(Queries.TWO_FACTOR_CODE_BY_CODE, domainClass);
        query.setParameter("code", code);

        return query.uniqueResultOptional();
    }
}
