package com.example.batch.reader.hibernate;

import com.example.batch.model.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.batch.item.database.HibernatePagingItemReader;
import org.springframework.batch.item.database.builder.HibernateCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.HibernatePagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class HibernatePayConfiguration {

    private final EntityManagerFactory emf;

    /**
     * cursor
     */
    @Bean
    public HibernateCursorItemReader<Pay> payHibernateCursorItemReader() {
        return new HibernateCursorItemReaderBuilder<Pay>()
                .name("hibernatePayCursorItemReader")
                .sessionFactory(emf.unwrap(SessionFactory.class))
                .queryString("from pay where amount>=:amount")
                .parameterValues(Collections.singletonMap("amount", 5000))
                .build();
    }

    /**
     * paging
     */
    @Bean
    public HibernatePagingItemReader<Pay> payHibernatePagingItemReader() {
        return new HibernatePagingItemReaderBuilder<Pay>()
                .name("hibernatePayPagingItemReader")
                .sessionFactory(emf.unwrap(SessionFactory.class))
                .queryString("from pay where amount>=:amount")
                .parameterValues(Collections.singletonMap("amount", 5000))
                .pageSize(100)
                .build();
    }

}
