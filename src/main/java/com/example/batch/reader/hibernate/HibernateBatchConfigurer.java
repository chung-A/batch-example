package com.example.batch.reader.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
@Slf4j
public class HibernateBatchConfigurer extends DefaultBatchConfigurer {

    @Resource(name = "sourceDataSource")
    public DataSource sourceDatasource;
    @Resource(name = "targetDataSource")
    public DataSource targetDataSource;

    private SessionFactory sessionFactory;
    private PlatformTransactionManager transactionManager;

    public HibernateBatchConfigurer( DataSource dataSource, EntityManagerFactory emf) {
        super(dataSource);
        this.sessionFactory=emf.unwrap(SessionFactory.class);
        this.transactionManager = new HibernateTransactionManager(this.sessionFactory);
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return this.transactionManager;
    }

}
