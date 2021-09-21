package com.example.batch.helper.chunk;

import com.example.batch.helper.jooq.JooqHibernateQueryProvider;
import org.jooq.Query;
import org.springframework.batch.item.database.orm.HibernateQueryProvider;

public class HibernateQueryProviderCreator {

    public static <T> HibernateQueryProvider<T> createJooqHibernateQueryProvider(Query query, Class<T> entityType){
        JooqHibernateQueryProvider<T> queryProvider=new JooqHibernateQueryProvider<T>() {
            @Override
            protected Query createJooqQuery() {
                return query;
            }

            @Override
            public void afterPropertiesSet() throws Exception {

            }
        };

        queryProvider.setEntityClass(entityType);
        return queryProvider;
    }
}
