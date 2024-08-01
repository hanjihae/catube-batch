package com.sparta.catubebatch.batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final JobLauncher jobLauncher;
    private final TaskExecutor taskExecutor;
    private final Job videoJob;
    private final Job adJob;

    public SchedulerConfig(JobLauncher jobLauncher,
                           Job videoJob, Job adJob,
                           @Qualifier("batchTaskExecutor") TaskExecutor taskExecutor) {
        this.jobLauncher = jobLauncher;
        this.videoJob = videoJob;
        this.adJob = adJob;
        this.taskExecutor = taskExecutor;
    }


//    @Scheduled(cron = "*/5 * * * * *") // 매 5초마다 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void runJobs() {
        try {
            System.out.println("[병렬] 잡 실행 시작");

            List<Runnable> tasks = new ArrayList<>();
            tasks.add(() -> runJob(videoJob, "병렬"));
            tasks.add(() -> runJob(adJob, "병렬"));

            CountDownLatch latch = new CountDownLatch(tasks.size());

            for (Runnable task : tasks) {
                taskExecutor.execute(() -> {
                    task.run();
                    latch.countDown();
                });
            }

            latch.await();

            // 요약 출력
            printSummary();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JobExecution runJob(Job job, String mode) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("uuid", UUID.randomUUID().toString())
                    .addString("mode", mode)
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            while (jobExecution.isRunning()) {
                Thread.sleep(100);
            }
            return jobExecution;
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void printSummary() {
        System.out.println("\n=== 배치 Job 병렬처리 테스트 Summary ===");
        if (CustomJobListener2.parallelStartTime > 0 && CustomJobListener2.parallelEndTime > 0) {
            long parallelTotalTime = CustomJobListener2.parallelEndTime - CustomJobListener2.parallelStartTime;
            System.out.println("병렬 총 소요시간: " + parallelTotalTime + " ms");
        }
    }
}
