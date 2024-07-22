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
    private final Job statJob;
    private final Job billJob;
    private final Job afterJob;

    private final Job videoStatJob;
    private final Job adStatJob;
    private final Job videoBillJob;
    private final Job adBillJob;
    private final Job videoAfterJob;
    private final Job adAfterJob;

    private final TaskExecutor taskExecutor;

    public SchedulerConfig(JobLauncher jobLauncher, Job statJob, Job billJob, Job afterJob,
                           Job videoStatJob, Job adStatJob, Job videoBillJob, Job adBillJob, Job videoAfterJob, Job adAfterJob,
                           @Qualifier("batchTaskExecutor") TaskExecutor taskExecutor) {
        this.jobLauncher = jobLauncher;
        this.statJob = statJob;
        this.billJob = billJob;
        this.afterJob = afterJob;
        this.videoStatJob = videoStatJob;
        this.adStatJob = adStatJob;
        this.videoBillJob = videoBillJob;
        this.adBillJob = adBillJob;
        this.videoAfterJob = videoAfterJob;
        this.adAfterJob = adAfterJob;
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(cron = "*/5 * * * * *") // 매 5초마다 실행
    public void runJobs() {
        try {
            // 순차 처리
            System.out.println("\n[순차] 잡 실행 시작");
            runSequentialJobs();

            // 병렬 처리
            System.out.println("[병렬] 잡 실행 시작\n");
            runParallelJobs();

            // 요약 출력
            printSummary();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runSequentialJobs() throws InterruptedException {
        JobExecution sequentialExecution1 = runJob(statJob, "순차");
        JobExecution sequentialExecution2 = runJob(billJob, "순차");
        JobExecution sequentialExecution3 = runJob(afterJob, "순차");

        while (sequentialExecution1.isRunning() || sequentialExecution2.isRunning() || sequentialExecution3.isRunning()) {
            Thread.sleep(100);
        }
    }

    private void runParallelJobs() throws InterruptedException {
        List<Runnable> tasks = new ArrayList<>();
        tasks.add(() -> runJob(videoStatJob, "병렬"));
        tasks.add(() -> runJob(adStatJob, "병렬"));

        CountDownLatch latch1 = new CountDownLatch(tasks.size());
        for (Runnable task : tasks) {
            taskExecutor.execute(() -> {
                task.run();
                latch1.countDown();
            });
        }
        latch1.await();

        // 다음 병렬 작업
        tasks.clear();
        tasks.add(() -> runJob(videoBillJob, "병렬"));
        tasks.add(() -> runJob(adBillJob, "병렬"));

        CountDownLatch latch2 = new CountDownLatch(tasks.size());
        for (Runnable task : tasks) {
            taskExecutor.execute(() -> {
                task.run();
                latch2.countDown();
            });
        }
        latch2.await();

        // 마지막 병렬 작업
        tasks.clear();
        tasks.add(() -> runJob(videoAfterJob, "병렬"));
        tasks.add(() -> runJob(adAfterJob, "병렬"));

        CountDownLatch latch3 = new CountDownLatch(tasks.size());
        for (Runnable task : tasks) {
            taskExecutor.execute(() -> {
                task.run();
                latch3.countDown();
            });
        }
        latch3.await();
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
        if (CustomJobListener.sequentialJobCount > 0) {
            System.out.println("순차 총 소요시간: " + CustomJobListener.sequentialTotalTime + " ms");
        }
        if (CustomJobListener.parallelStartTime > 0 && CustomJobListener.parallelEndTime > 0) {
            long parallelTotalTime = CustomJobListener.parallelEndTime - CustomJobListener.parallelStartTime;
            System.out.println("병렬 총 소요시간: " + parallelTotalTime + " ms");
        }
        System.out.println("순차와 병렬의 총 소요시간 차이: " + (CustomJobListener.parallelEndTime - CustomJobListener.parallelStartTime - CustomJobListener.sequentialTotalTime) * (-1) + " ms");
    }
}
