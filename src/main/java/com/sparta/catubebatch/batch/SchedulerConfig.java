package com.sparta.catubebatch.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {
    private final JobLauncher jobLauncher;
    private final Job statJob;
    private final Job billJob;
    private final Job afterJob;

//    @Scheduled(cron = "*/5 * * * * *") // 매 5초마다 실행
    @Scheduled(cron = "0 0 0 * * ?") // 매일 00시에 실행
    public void performDailyJob() throws Exception {
        jobLauncher.run(statJob, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
        jobLauncher.run(billJob, new JobParametersBuilder().addLong("time", System.currentTimeMillis() + 1).toJobParameters());
        jobLauncher.run(afterJob, new JobParametersBuilder().addLong("time", System.currentTimeMillis() + 2).toJobParameters());
    }
}
