package com.example.batch.config.db;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.example.batch.config.db.TargetDatabaseProperties.*;

@ConfigurationProperties(prefix = TARGET_DATABASE_PREFIX)
public class TargetDatabaseProperties extends DataBaseProperties{
    public static final String TARGET_DATABASE_PREFIX = "target.datasource.master";
}
