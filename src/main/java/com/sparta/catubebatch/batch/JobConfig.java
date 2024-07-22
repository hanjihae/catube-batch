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

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class JobConfig {
    private final JobRepository jobRepository;
    private final CustomJobListener listener;

    @Bean
    public Job videoStatJob(Step videoStatStep) {
        return new JobBuilder("videoStatJob", jobRepository)
                .preventRestart()
                .listener(listener)
                .start(videoStatStep)
                .build();
    }

    @Bean
    public Job adStatJob(Step adStatStep) {
        return new JobBuilder("adStatJob", jobRepository)
                .preventRestart()
                .listener(listener)
                .start(adStatStep)
                .build();
    }

    @Bean
    public Job videoBillJob(Step videoBillStep) {
        return new JobBuilder("videoBillJob", jobRepository)
                .preventRestart()
                .listener(listener)
                .start(videoBillStep)
                .build();
    }

    @Bean
    public Job adBillJob(Step adBillStep) {
        return new JobBuilder("adBillJob", jobRepository)
                .preventRestart()
                .listener(listener)
                .start(adBillStep)
                .build();
    }

    @Bean
    public Job videoAfterJob(Step videoDoneStep) {
        return new JobBuilder("videoAfterJob", jobRepository)
                .preventRestart()
                .listener(listener)
                .start(videoDoneStep)
                .build();
    }

    @Bean
    public Job adAfterJob(Step adDoneStep) {
        return new JobBuilder("adAfterJob", jobRepository)
                .preventRestart()
                .listener(listener)
                .start(adDoneStep)
                .build();
    }

    // 병렬처리 전
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
