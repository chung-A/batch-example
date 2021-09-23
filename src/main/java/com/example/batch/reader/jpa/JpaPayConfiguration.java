package com.example.batch.reader.jpa;

import com.example.batch.model.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class JpaPayConfiguration {

    private final EntityManagerFactory emf;

    @Bean
    public JpaPagingItemReader<Pay> payJpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<Pay>()
                .name("payJpaPagingItemReader")
                .entityManagerFactory(emf)
                .queryString("select p from pay p where p.amount>=:amount")
                .parameterValues(Collections.singletonMap("amount", 5000))
                .build();
    }

    @Bean
    public JpaPagingItemReader<Pay> payJpaPagingItemReaderUsingQueryProvider() {
        PayByAmountQueryProvider provider = new PayByAmountQueryProvider();
        provider.setAmount(5000L);

        return new JpaPagingItemReaderBuilder<Pay>()
                .name("payJpaPagingItemReader")
                .entityManagerFactory(emf)
                .queryProvider(provider)
                .build();
    }
}
