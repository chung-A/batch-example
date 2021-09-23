package com.example.batch.reader.jdbc;

import com.example.batch.model.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class JdbcPayConfiguration {

    @Resource(name = "sourceDataSource")
    public DataSource sourceDatasource;
    @Resource(name = "targetDataSource")
    public DataSource targetDataSource;

    /**
     * Cursor
     */
    @Bean
    public JdbcCursorItemReader<Pay> payJdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Pay>()
                .name("payJdbcCursorItemReader")
                .dataSource(sourceDatasource)
                .sql("select * from pay where amount>= 5000")
                .rowMapper(new PayRowMapper())
                .build();
    }

    /**
     * Paging
     */
    @Bean
    public JdbcPagingItemReader<Pay> payItemReader(PagingQueryProvider queryProvider) {
        return new JdbcPagingItemReaderBuilder<Pay>()
                .name("payJdbcPagingItemReader")
                .dataSource(sourceDatasource)
                .queryProvider(queryProvider)
                .pageSize(10)
                .rowMapper(new PayRowMapper())
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean pagingQueryProvider() {
        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();

        factoryBean.setSelectClause("select * ");
        factoryBean.setFromClause("from pay ");
        factoryBean.setWhereClause("where amount>=5000");
        factoryBean.setSortKey("id");
        factoryBean.setDataSource(sourceDatasource);

        return factoryBean;
    }
}
