package com.sparta.catubebatch.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BatchConfig {

    @Bean(name = "batchTaskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4); // 기본 스레드 수
        taskExecutor.setMaxPoolSize(10); // 최대 스레드 수
        taskExecutor.setQueueCapacity(50); // 대기 큐 크기
        taskExecutor.setThreadNamePrefix("Batch-Executor-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
