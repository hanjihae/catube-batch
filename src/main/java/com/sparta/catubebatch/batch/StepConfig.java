package com.sparta.catubebatch.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class StepConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final TaskletConfig taskletConfig;

    @Bean
    public Step videoStatStep() {
        return new StepBuilder("videoStatStep", jobRepository)
                .tasklet(taskletConfig.videoStatTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step adStatStep() {
        return new StepBuilder("adStatStep", jobRepository)
                .tasklet(taskletConfig.adStatTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step videoBillStep() {
        return new StepBuilder("videoBillStep", jobRepository)
                .tasklet(taskletConfig.videoBillTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step adBillStep() {
        return new StepBuilder("adBillStep", jobRepository)
                .tasklet(taskletConfig.adBillTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step videoDoneStep() {
        return new StepBuilder("videoDoneStep", jobRepository)
                .tasklet(taskletConfig.videoDoneTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step adDoneStep() {
        return new StepBuilder("adDoneStep", jobRepository)
                .tasklet(taskletConfig.adDoneTasklet(), platformTransactionManager)
                .build();
    }
}
