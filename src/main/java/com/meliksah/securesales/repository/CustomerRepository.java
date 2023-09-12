package com.meliksah.securesales.repository;

import com.meliksah.securesales.common.BaseRepository;
import com.meliksah.securesales.domain.Customer;
import com.meliksah.securesales.domain.Stats;
import com.meliksah.securesales.utils.Queries;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author mselvi
 * @Created 23.08.2023
 */

@Repository
public class CustomerRepository extends BaseRepository<Customer, Long> {
    public CustomerRepository() {
        super(Customer.class);
    }

    public List<Customer> findAllCustomerByName(String name, Integer page, Integer size) {
        Query<Customer> query = getCurrentSession().createQuery(Queries.CUSTOMERS_BY_NAME, domainClass);

        query.setParameter("name", name);

        query.setFirstResult(page);
        query.setMaxResults(size);

        return query.getResultList();
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        Query<Customer> query = getCurrentSession().createQuery(Queries.CUSTOMER_BY_EMAIL, domainClass);
        query.setParameter("email",email);

        return query.uniqueResultOptional();
    }

    public Optional<Stats> getStats() {
        Query<Stats> query = getCurrentSession().createQuery(Queries.GET_STATS, Stats.class);
        query.setResultTransformer(Transformers.aliasToBean(Stats.class));
        return query.uniqueResultOptional();
    }
}
