package com.example.batch.job;

import com.example.batch.model.Pay;
import com.example.batch.model.PayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class DatabaseJobTest {

    @Resource(name = "sourceDataSource")
    public DataSource sourceDatasource;
    @Resource(name = "targetDataSource")
    public DataSource targetDataSource;

    public static int NOW_COUNT = 0;
    public static int CHUNK_SIZE = 5000;
    public final int SAVE_COUNT = 10000;

    private final String DATA_SOURCE_TEST_JOB = "datasourcetestjob";
    private final String DATA_SOURCE_TEST_ADD_STEP = "datasourcetestaddstep";
    private final String DATA_SOURCE_TEST_STEP = "datasourceteststep";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private ExecuteType executeType = ExecuteType.SOURCE;

    @Bean
    public Job test() {
        NOW_COUNT = 0;

        return jobBuilderFactory.get(DATA_SOURCE_TEST_JOB)
                .incrementer(new RunIdIncrementer())
                .start(doStep(null))
//                .next(decider())
//                .from(decider())
//                    .on("CONTINUE")
//                    .to(doStep(null))
//                .end()
                .build();
    }

    @Bean
    @JobScope
    public Step doStep(@Value("#{jobParameters[type]}") String type) {
        if (StringUtils.hasText(type) && "target".equals(type)) {
            executeType = ExecuteType.TARGET;
        }

        return stepBuilderFactory.get(DATA_SOURCE_TEST_STEP)
                .<Pay, Pay>chunk(CHUNK_SIZE)
                .reader(itemReader())
//                .processor()
                .writer(writer())
                .build();
    }

    @Bean
    public ItemReader<Pay> itemReader() {
        List<Pay> list = new ArrayList<>();
        Random r = new Random();

        String txName = executeType == ExecuteType.SOURCE ? "source" : "target";

        for (int i = 0; i < CHUNK_SIZE; i++) {
            list.add(new Pay((long) r.nextInt(10000), txName,
                    LocalDateTime.now()
                            .minusDays(r.nextInt(100))
                            .minusHours(r.nextInt(24))
                            .minusMinutes(r.nextInt(60))
                            .minusSeconds(r.nextInt(60))
                    )
            );
        }

        return new ListItemReader<>(list);
    }

    @Bean
    public ItemWriter<Pay> writer() {
        DataSource ds = sourceDatasource;
        if (executeType == ExecuteType.TARGET) {
            ds = targetDataSource;
        }

        return new JdbcBatchItemWriterBuilder<Pay>()
                .dataSource(ds)
                .sql("insert into pay(amount,tx_date_time,tx_name) values (:amount,:txDateTime,:txName)")
                .beanMapped()
                .build();
    }

    private final PayRepository payRepository;

    @Bean
    public JobExecutionDecider decider() {
        return new JobExecutionDecider() {
            @Override
            public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
                long count = payRepository.count();

                log.info("NOW COUNT={}", count);
                NOW_COUNT += CHUNK_SIZE;

                if (NOW_COUNT >= SAVE_COUNT) {
                    return new FlowExecutionStatus("OK");
                }
                else{
                    return new FlowExecutionStatus("CONTINUE");
                }
            }
        };
    }


    public enum ExecuteType{
        SOURCE,TARGET
    }
}
