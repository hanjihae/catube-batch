package com.sparta.catubebatch.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class StepConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final TaskletConfig taskletConfig;

    @Bean
    public VirtualThreadTaskExecutor taskExecutor() {
        return new VirtualThreadTaskExecutor("spring-batch-");
    }

    @Bean
    public Step videoStatStep(VirtualThreadTaskExecutor taskExecutor) {
        return new StepBuilder("videoStatStep", jobRepository)
                .tasklet(taskletConfig.videoStatTasklet(), platformTransactionManager)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step adStatStep(VirtualThreadTaskExecutor taskExecutor) {
        return new StepBuilder("adStatStep", jobRepository)
                .tasklet(taskletConfig.adStatTasklet(), platformTransactionManager)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step videoBillStep(VirtualThreadTaskExecutor taskExecutor) {
        return new StepBuilder("videoBillStep", jobRepository)
                .tasklet(taskletConfig.videoBillTasklet(), platformTransactionManager)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step adBillStep(VirtualThreadTaskExecutor taskExecutor) {
        return new StepBuilder("adBillStep", jobRepository)
                .tasklet(taskletConfig.adBillTasklet(), platformTransactionManager)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step videoDoneStep(VirtualThreadTaskExecutor taskExecutor) {
        return new StepBuilder("videoDoneStep", jobRepository)
                .tasklet(taskletConfig.videoDoneTasklet(), platformTransactionManager)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step adDoneStep(VirtualThreadTaskExecutor taskExecutor) {
        return new StepBuilder("adDoneStep", jobRepository)
                .tasklet(taskletConfig.adDoneTasklet(), platformTransactionManager)
                .taskExecutor(taskExecutor)
                .build();
    }
}
