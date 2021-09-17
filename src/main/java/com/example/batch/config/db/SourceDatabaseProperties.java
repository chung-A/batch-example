package com.example.batch.config.db;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.example.batch.config.db.SourceDatabaseProperties.*;

@ConfigurationProperties(prefix = SOURCE_DATABASE_PREFIX)
public class SourceDatabaseProperties extends DataBaseProperties {

    public static final String SOURCE_DATABASE_PREFIX = "source.datasource.master";
}
