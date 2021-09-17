package com.example.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleJobConfiguration {

    private final String LOG_PRINT_JOB = "LOG_PRINT_JOB";
    private final String LOG_PRINT_STEP = "LOG_PRINT_STEP";
    private final String WORK_STEP = "WORK_STEP";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job logPrintJob() {
        return jobBuilderFactory.get(LOG_PRINT_JOB)
                .start(logPrintStep(null))
                .next(doWork(null))
                .build();
    }

    @Bean
    @JobScope
    public Step logPrintStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get(WORK_STEP)
                .tasklet(((contribution, chunkContext) -> {
//                    throw new IllegalArgumentException("step1 error!");
                    log.info("Job start!! date = {}",requestDate);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    @JobScope
    public Step doWork(@Value("#{jobParameters[requestDate]}")String requestDate) {
        return stepBuilderFactory.get(WORK_STEP)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("Oh, I'm now Working!!!! Don't Look at me!! {}", requestDate);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
