package com.sparta.catubebatch.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class JobConfig {
    private final JobRepository jobRepository;
    private final JobCompletionNotificationListener listener;

    @Bean
    public Job statJob(Step videoStatStep, Step adStatStep) {
        return new JobBuilder("statJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(videoStatStep)
                .next(adStatStep)
                .build();
    }

    @Bean
    public Job billJob(Step videoBillStep, Step adBillStep) {
        return new JobBuilder("billJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(videoBillStep)
                .next(adBillStep)
                .build();
    }

    @Bean
    public Job afterJob(Step videoDoneStep, Step adDoneStep) {
        return new JobBuilder("afterJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(videoDoneStep)
                .next(adDoneStep)
                .build();
    }
}
