package com.meliksah.securesales.repository;

import com.meliksah.securesales.common.BaseRepository;
import com.meliksah.securesales.domain.ResetPasswordVerification;
import com.meliksah.securesales.utils.Queries;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 18.08.2023
 */

@Repository
public class ResetPasswordVerificationRepository extends BaseRepository<ResetPasswordVerification, Long> {

    public ResetPasswordVerificationRepository() {
        super(ResetPasswordVerification.class);
    }

    public void deleteByUserId(Long userId) {
        Query query = getCurrentSession().createQuery(Queries.DELETE_PASSWORD_VERIFICATION_BY_USER_UD);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    public Optional<ResetPasswordVerification> findByUrl(String url) {
        Query<ResetPasswordVerification> query = getCurrentSession().createQuery(Queries.PASSWORD_VERIFICATION_CODE_BY_URL, domainClass);
        query.setParameter("url", url);

        return query.uniqueResultOptional();
    }
}
