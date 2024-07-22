//package com.sparta.catubebatch.batch;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.partition.support.SimplePartitioner;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Configuration
//@RequiredArgsConstructor
//@EnableBatchProcessing
//public class StepConfig {
//    private final JobRepository jobRepository;
//    private final PlatformTransactionManager platformTransactionManager;
//    private final TaskletConfig taskletConfig;
//
//    @Bean
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(10);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(25);
//        executor.setThreadNamePrefix("partitioned-task-");
//        executor.initialize();
//        return executor;
//    }
//
//    @Bean
//    public Step statStep() {
//        return new StepBuilder("statStep", jobRepository)
//                .partitioner(videoStatStep().getName(), new SimplePartitioner())
//                .partitioner(adStatStep().getName(), new SimplePartitioner())
//                .step(videoStatStep())
//                .step(adStatStep())
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @Bean
//    public Step billStep() {
//        return new StepBuilder("billStep", jobRepository)
//                .partitioner(videoBillStep().getName(), new SimplePartitioner())
//                .partitioner(adBillStep().getName(), new SimplePartitioner())
//                .step(videoBillStep())
//                .step(adBillStep())
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @Bean
//    public Step doneStep() {
//        return new StepBuilder("doneStep", jobRepository)
//                .partitioner(videoDoneStep().getName(), new SimplePartitioner())
//                .partitioner(adDoneStep().getName(), new SimplePartitioner())
//                .step(videoDoneStep())
//                .step(adDoneStep())
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @Bean
//    public Step videoStatStep() {
//        return new StepBuilder("videoStatStep", jobRepository)
//                .tasklet(taskletConfig.videoStatTasklet(), platformTransactionManager)
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @Bean
//    public Step adStatStep() {
//        return new StepBuilder("adStatStep", jobRepository)
//                .tasklet(taskletConfig.adStatTasklet(), platformTransactionManager)
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @Bean
//    public Step videoBillStep() {
//        return new StepBuilder("videoBillStep", jobRepository)
//                .tasklet(taskletConfig.videoBillTasklet(), platformTransactionManager)
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @Bean
//    public Step adBillStep() {
//        return new StepBuilder("adBillStep", jobRepository)
//                .tasklet(taskletConfig.adBillTasklet(), platformTransactionManager)
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @Bean
//    public Step videoDoneStep() {
//        return new StepBuilder("videoDoneStep", jobRepository)
//                .tasklet(taskletConfig.videoDoneTasklet(), platformTransactionManager)
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @Bean
//    public Step adDoneStep() {
//        return new StepBuilder("adDoneStep", jobRepository)
//                .tasklet(taskletConfig.adDoneTasklet(), platformTransactionManager)
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//}
