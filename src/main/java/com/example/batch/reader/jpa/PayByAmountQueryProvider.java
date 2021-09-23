package com.example.batch.reader.jpa;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class PayByAmountQueryProvider extends AbstractJpaQueryProvider {

    private Long amount;

    @Override
    public Query createQuery() {
        EntityManager em = getEntityManager();

        Query query = em.createQuery("select p from Pay p where p.amount>=:amount");
        query.setParameter("amount", amount);

        return query;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(amount,"Amount is required");
    }
}
