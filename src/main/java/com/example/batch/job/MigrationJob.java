package com.example.batch.job;

import com.example.batch.model.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MigrationJob {

    @Resource(name = "sourceDataSource")
    public DataSource sourceDatasource;
    @Resource(name = "targetDataSource")
    public DataSource targetDataSource;

    private final String MIGRATION1_JOB = "migration1job";
    private final String MIGRATION1_STEP = "migrationstep1";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final int CHUNK_SIZE = 500;

    @Bean
    public Job migrationJob() {
        return jobBuilderFactory.get(MIGRATION1_JOB)
                .start(migrationStep1())
                .build();
    }

    public Step migrationStep1() {
        return stepBuilderFactory.get(MIGRATION1_STEP)
                .<Pay,Pay>chunk(CHUNK_SIZE)
                .reader()
                .
    }
}
