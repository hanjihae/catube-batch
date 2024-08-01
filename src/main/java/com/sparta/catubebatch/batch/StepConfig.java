package com.sparta.catubebatch.batch;

import com.sparta.catubebatch.dto.AdStatData;
import com.sparta.catubebatch.dto.VideoBillData;
import com.sparta.catubebatch.dto.VideoStatData;
import com.sparta.catubebatch.entity.*;
import com.sparta.catubebatch.itemprocessor.*;
import com.sparta.catubebatch.itemreader.*;
import com.sparta.catubebatch.itemwriter.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class StepConfig {  // chunk
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    // videoStat
    private final VideoStatItemReader videoStatItemReader;
    private final VideoStatItemProcessor videoStatItemProcessor;
    private final VideoStatItemWriter videoStatItemWriter;
    // adStat
    private final AdStatItemReader adStatItemReader;
    private final AdStatItemProcessor adStatItemProcessor;
    private final AdStatItemWriter adStatItemWriter;
    // videoBill
    private final VideoBillItemReader videoBillItemReader;
    private final VideoBillItemProcessor videoBillItemProcessor;
    private final VideoBillItemWriter videoBillItemWriter;
    // adBill
    private final AdBillItemReader adBillItemReader;
    private final AdBillItemProcessor adBillItemProcessor;
    private final AdBillItemWriter adBillItemWriter;
    // videoDone
    private final VideoBillDoneItemReader videoBillDoneItemReader;
    private final VideoBillDoneItemProcessor videoBillDoneItemProcessor;
    private final VideoBillDoneItemWriter videoBillDoneItemWriter;
    // adDone
    private final AdBillDoneItemReader adBillDoneItemReader;
    private final AdBillDoneItemProcessor adBillDoneItemProcessor;
    private final AdBillDoneItemWriter adBillDoneItemWriter;

    @Bean
    public Step videoStatStep() {
        return new StepBuilder("videoStatStep", jobRepository)
                .<VideoStatData, VideoStat>chunk(1000, platformTransactionManager)
                .reader(videoStatItemReader)
                .processor(videoStatItemProcessor)
                .writer(videoStatItemWriter)
                .build();
    }

    @Bean
    public Step adStatStep() {
        return new StepBuilder("adStatStep", jobRepository)
                .<AdStatData, AdStat>chunk(1000, platformTransactionManager)
                .reader(adStatItemReader)
                .processor(adStatItemProcessor)
                .writer(adStatItemWriter)
                .build();
    }


    @Bean
    public Step videoBillStep() {
        return new StepBuilder("videoBillStep", jobRepository)
                .<VideoBillData, VideoBill>chunk(1000, platformTransactionManager)
                .reader(videoBillItemReader)
                .processor(videoBillItemProcessor)
                .writer(videoBillItemWriter)
                .build();
    }

    @Bean
    public Step adBillStep() {
        return new StepBuilder("adBillStep", jobRepository)
                .<Map.Entry<VideoAd, Integer>, AdBill>chunk(1000, platformTransactionManager)
                .reader(adBillItemReader)
                .processor(adBillItemProcessor)
                .writer(adBillItemWriter)
                .build();
    }

    @Bean
    public Step videoDoneStep() {
        return new StepBuilder("videoDoneStep", jobRepository)
                .<Map.Entry<Video, Integer>, Video>chunk(1000, platformTransactionManager)
                .reader(videoBillDoneItemReader)
                .processor(videoBillDoneItemProcessor)
                .writer(videoBillDoneItemWriter)
                .build();
    }

    @Bean
    public Step adDoneStep() {
        return new StepBuilder("adDoneStep", jobRepository)
                .<Map.Entry<Long, Integer>, VideoAd>chunk(1000, platformTransactionManager)
                .reader(adBillDoneItemReader)
                .processor(adBillDoneItemProcessor)
                .writer(adBillDoneItemWriter)
                .build();
    }
}
