package com.sparta.catubebatch.batch;

import com.sparta.catubebatch.service.BillBatchService;
import com.sparta.catubebatch.service.StatBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class TaskletConfig {
    private final StatBatchService statBatchService;
    private final BillBatchService billbatchService;

    @Bean
    public Tasklet videoStatTasklet() {
        return (contribution, chunkContext) -> {
            statBatchService.saveDailyStat();
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet adStatTasklet() {
        return (contribution, chunkContext) -> {
            statBatchService.saveAdViewCount();
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet videoBillTasklet() {
        return (contribution, chunkContext) -> {
            billbatchService.calculateVideoAmount();
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet adBillTasklet() {
        return (contribution, chunkContext) -> {
            billbatchService.calculateAdAmount();
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet videoDoneTasklet() {
        return (contribution, chunkContext) -> {
            billbatchService.saveTodayVideoBillDone();
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet adDoneTasklet() {
        return (contribution, chunkContext) -> {
            billbatchService.saveTodayAdBillDone();
            return RepeatStatus.FINISHED;
        };
    }
}
