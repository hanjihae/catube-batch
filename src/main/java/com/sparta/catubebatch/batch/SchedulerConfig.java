//package com.sparta.catubebatch.batch;
//
//import org.springframework.batch.core.*;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
//import org.springframework.batch.core.repository.JobRestartException;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.CountDownLatch;
//
//@Configuration
//@EnableScheduling
//public class SchedulerConfig {
//
//    private final JobLauncher jobLauncher;
//    private final List<List<Job>> parallelJobsStages;
//    private final TaskExecutor taskExecutor;
//
//    public SchedulerConfig(JobLauncher jobLauncher,
//                           Job videoStatJob, Job adStatJob, Job videoBillJob, Job adBillJob,
//                           Job videoAfterJob, Job adAfterJob,
//                           @Qualifier("batchTaskExecutor") TaskExecutor taskExecutor) {
//        this.jobLauncher = jobLauncher;
//        this.taskExecutor = taskExecutor;
//
//        // 병렬 작업을 단계별로 그룹화하여 관리
//        this.parallelJobsStages = Arrays.asList(
//                Arrays.asList(videoStatJob, adStatJob),
//                Arrays.asList(videoBillJob, adBillJob),
//                Arrays.asList(videoAfterJob, adAfterJob)
//        );
//    }
//
//    @Scheduled(cron = "*/5 * * * * *") // 매 5초마다 실행
//    public void runJobs() {
//        try {
//            // 병렬 처리
//            System.out.println("[병렬] 잡 실행 시작\n");
//            runParallelJobs();
//
//            // 요약 출력
//            printParSummary();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void runParallelJobs() throws InterruptedException {
//        for (List<Job> jobStage : parallelJobsStages) {
//            runJobStage(jobStage, "병렬");
//        }
//    }
//
//    private void runJobStage(List<Job> jobs, String mode) throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(jobs.size());
//        for (Job job : jobs) {
//            taskExecutor.execute(() -> {
//                runJob(job, mode);
//                latch.countDown();
//            });
//        }
//        latch.await();
//    }
//
//    private JobExecution runJob(Job job, String mode) {
//        try {
//            JobParameters jobParameters = new JobParametersBuilder()
//                    .addString("uuid", UUID.randomUUID().toString())
//                    .addString("mode", mode)
//                    .toJobParameters();
//
//            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
//            while (jobExecution.isRunning()) {
//                Thread.sleep(100);
//            }
//            return jobExecution;
//        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
//                 JobParametersInvalidException | InterruptedException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private void printParSummary() {
//        if (CustomJobListener.parallelStartTime > 0 && CustomJobListener.parallelEndTime > 0) {
//            System.out.println("\n=== 병렬 처리 Summary ===");
//            long parallelTotalTime = CustomJobListener.parallelEndTime - CustomJobListener.parallelStartTime;
//            System.out.println("병렬 총 소요시간: " + parallelTotalTime + " ms");
//        } else {
//            System.out.println("\n병렬 처리 작업이 수행되지 않았습니다.");
//        }
//    }
//
//}
