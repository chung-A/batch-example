package com.example.batch.helper.jooq;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.springframework.batch.item.database.orm.AbstractHibernateQueryProvider;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
@Getter
@Setter
public abstract class JooqHibernateQueryProvider<E> extends AbstractHibernateQueryProvider implements InitializingBean {

    private Class<E> entityClass;

    @Override
    public Query<E> createQuery() {
        log.debug(createJooqQuery().toString());
        if (isStatelessSession()) {
            return null;
        }
        return null;
    }

    protected abstract org.jooq.Query createJooqQuery();

    public abstract void afterPropertiesSet() throws Exception;
}
