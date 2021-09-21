package com.example.batch.config;

import com.example.batch.config.db.DataBaseProperties;
import com.example.batch.config.db.SourceDatabaseProperties;
import com.example.batch.config.db.TargetDatabaseProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties({SourceDatabaseProperties.class, TargetDatabaseProperties.class})
@PropertySource(value = {"classpath:/db/properties/${spring.profiles.active}.properties"})
@RequiredArgsConstructor
@Slf4j
public class DatabaseConfiguration {

    @Primary
    @Bean(name = "targetDataSource")
    public DataSource targetDataSource(TargetDatabaseProperties databaseProperties) {
        return getDataSource(databaseProperties);
    }

    @Bean("sourceDataSource")
    public DataSource sourceDataSource(SourceDatabaseProperties databaseProperties) {
        return getDataSource(databaseProperties);
    }

    protected DataSource getDataSource(DataBaseProperties databaseProperties) {
        HikariDataSource dataSource = new HikariDataSource();
        if (databaseProperties.isReadOnly()) {
            dataSource.setPoolName("HikariPool-purchase-slave");
        } else {
            dataSource.setPoolName("HikariPool-purchase-master");
        }
        configureDataSource(dataSource, databaseProperties);
        return dataSource;
    }

    /**
     * - reference http://brettwooldridge.github.io/HikariCP/ https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
     */
    protected void configureDataSource(HikariDataSource dataSource, DataBaseProperties databaseProperties) {
        dataSource.setConnectionTestQuery("select 1");
        dataSource.setReadOnly(databaseProperties.isReadOnly());
        dataSource.setDriverClassName(databaseProperties.getDriverClassName());
        dataSource.setJdbcUrl(databaseProperties.getUrl());
        dataSource.setUsername(databaseProperties.getUserName());
        dataSource.setPassword(databaseProperties.getPassword());
        dataSource.setMaximumPoolSize(databaseProperties.getMaximumPoolSize());
        dataSource.setConnectionTimeout(databaseProperties.getTimeOut());
        dataSource.setMinimumIdle(databaseProperties.getMinimumIdle());
        dataSource.addDataSourceProperty("cachePrepStmts", "true");
        dataSource.addDataSourceProperty("prepStmtCacheSize", "250");
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource.addDataSourceProperty("queryInterceptors", "com.example.batch.config.db.interceptors.CustomQueryInterceptor");
    }
}
