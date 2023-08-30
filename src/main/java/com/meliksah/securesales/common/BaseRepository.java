package com.meliksah.securesales.common;

import com.meliksah.securesales.enumeration.ErrorMessage;
import com.meliksah.securesales.exception.ApiException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 * @Author mselvi
 * @Created 30.08.2023
 */

public abstract class BaseRepository<E extends BaseEntity, ID> {

    protected Class<E> domainClass;

    @Autowired
    private SessionFactory sessionFactory;

    public BaseRepository(Class<E> domainClass) {
        this.domainClass = domainClass;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<E> findAll() {
        Query<E> query = geteCriteriaQuery();

        return query.getResultList();
    }

    private Query<E> geteCriteriaQuery() {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(domainClass);
        Root<E> root = criteriaQuery.from(domainClass);
        criteriaQuery.select(root);

        Query<E> query = getCurrentSession().createQuery(criteriaQuery);
        return query;
    }

    public List<E> findAll(Integer page, Integer size) {

        Query<E> query = geteCriteriaQuery();

        query.setFirstResult(page);
        query.setMaxResults(size);

        return query.getResultList();
    }

    public Optional<E> findById(ID id) {
        if (id == null) {
            throw new ApiException(ErrorMessage.ID_MUST_NOT_NULL);
        }
        return Optional.ofNullable(getCurrentSession().find(domainClass, id));
    }

    public E save(E entity) {

        entity = getCurrentSession().merge(entity);

        return entity;
    }
}
