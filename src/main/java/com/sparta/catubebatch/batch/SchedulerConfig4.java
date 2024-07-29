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

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Configuration
@EnableScheduling
public class SchedulerConfig4 {

    private final JobLauncher jobLauncher;
    private final TaskExecutor taskExecutor;
    private final Job videoJob;
    private final Job adJob;

    public SchedulerConfig4(JobLauncher jobLauncher,
                            Job videoJob, Job adJob,
                            @Qualifier("batchTaskExecutor") TaskExecutor taskExecutor) {
        this.jobLauncher = jobLauncher;
        this.videoJob = videoJob;
        this.adJob = adJob;
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(cron = "*/5 * * * * *") // 매 5초마다 실행
    public void runJobs() {
        try {
            System.out.println("[병렬] 잡 실행 시작");

            // 병렬 작업 시작 시간 기록
            CustomJobListener.parallelStartTime = System.currentTimeMillis();

            // CountDownLatch를 사용하여 병렬 작업 완료 대기
            CountDownLatch latch = new CountDownLatch(2);

            // 병렬 처리
            taskExecutor.execute(() -> {
                executeJob(videoJob, "병렬");
                latch.countDown(); // videoJob 완료 시 카운트다운
            });
            taskExecutor.execute(() -> {
                executeJob(adJob, "병렬");
                latch.countDown(); // adJob 완료 시 카운트다운
            });

            // 모든 병렬 작업이 완료될 때까지 대기
            latch.await();
            CustomJobListener.parallelEndTime = System.currentTimeMillis(); // 전체 병렬 작업 종료 시간 기록

            // 요약 출력
            printSummary();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeJob(Job job, String mode) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("uuid", UUID.randomUUID().toString())
                    .addString("mode", mode)
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            while (jobExecution.isRunning()) {
                Thread.sleep(100);
            }
            System.out.println(job.getName() + " 실행 완료.");

        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printSummary() {
        long startTime = CustomJobListener.parallelStartTime;
        long endTime = CustomJobListener.parallelEndTime;
        if (startTime > 0 && endTime > 0) {
            System.out.println("\n=== 병렬 처리 요약 ===");
            System.out.println("병렬 총 소요시간: " + (endTime - startTime) + " ms");
            System.out.println("잡 병렬로 실행 완료");
        } else {
            System.out.println("\n병렬 처리 작업이 수행되지 않았습니다.");
        }
    }
}
