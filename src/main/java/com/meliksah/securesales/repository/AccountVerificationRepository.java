package com.meliksah.securesales.repository;


import com.meliksah.securesales.common.BaseRepository;
import com.meliksah.securesales.domain.AccountVerification;
import com.meliksah.securesales.utils.Queries;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @Author mselvi
 * @Created 09.08.2023
 */

@Repository
public class AccountVerificationRepository extends BaseRepository<AccountVerification, Long> {
    public AccountVerificationRepository() {
        super(AccountVerification.class);
    }

    public void deleteByUrl(String url) {
        Query query = getCurrentSession().createQuery(Queries.DELETE_ACCOUNT_VERIFICATION_URL);
        query.setParameter("url", url);
        query.executeUpdate();
    }
}
