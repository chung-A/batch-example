package com.example.batch.job;

import com.example.batch.model.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class DatabaseJobTest {

    @Resource(name = "sourceDataSource")
    public DataSource sourceDatasource;
    public static int CHUNK_SIZE = 500;

    private final String DATA_SOURCE_TEST_JOB = "datasourcetestjob";
    private final String DATA_SOURCE_TEST_STEP = "datasourceteststep";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job test() {
        return jobBuilderFactory.get(DATA_SOURCE_TEST_JOB)
                .start(doStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step doStep() {
        return stepBuilderFactory.get(DATA_SOURCE_TEST_STEP)
                .<Pay, Pay>chunk(CHUNK_SIZE)
                .reader(jdbcCursorItemReader())
                .writer(jdbcCursorItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Pay> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Pay>()
                .fetchSize(CHUNK_SIZE)
                .dataSource(sourceDatasource)
                .rowMapper(new BeanPropertyRowMapper<>(Pay.class))
                .sql("SELECT * FROM BATCH_JOB_EXECUTION_CONTEXT;")
                .name("jdbcCursorItemReader")
                .build();
    }

    @Bean
    public ItemWriter<Pay> jdbcCursorItemWriter() {
        return list ->{
            for (Pay pay : list) {
                log.info("current pay is {}", pay);
            }
        };
    }
}
