//package com.sparta.catubebatch.batch;
//
//import org.springframework.batch.core.*;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
//import org.springframework.batch.core.repository.JobRestartException;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.util.Objects;
//import java.util.UUID;
//
//@Configuration
//@EnableScheduling
//public class SchedulerConfig2 {   // 순차
//
//    private final JobLauncher jobLauncher;
//    private final Job statJob;
//    private final Job billJob;
//    private final Job afterJob;
//
//
//    public SchedulerConfig2(JobLauncher jobLauncher,
//                            Job statJob, Job billJob, Job afterJob) {
//        this.jobLauncher = jobLauncher;
//        this.statJob = statJob;
//        this.billJob = billJob;
//        this.afterJob = afterJob;
//    }
//
//    @Scheduled(cron = "*/5 * * * * *") // 매 5초마다 실행
//    public void runJobs() {
//        try {
//            // 순차 처리
//            System.out.println("\n[순차] 잡 실행 시작");
//            JobExecution sequentialExecution1 = runJob(statJob, "순차");
//            Objects.requireNonNull(sequentialExecution1).getJobParameters();
//            JobExecution sequentialExecution2 = runJob(billJob, "순차");
//            Objects.requireNonNull(sequentialExecution2).getJobParameters();
//            JobExecution sequentialExecution3 = runJob(afterJob, "순차");
//            Objects.requireNonNull(sequentialExecution2).getJobParameters();
//
//            while (sequentialExecution1.isRunning()|| sequentialExecution2.isRunning() || sequentialExecution3.isRunning()) {
//                Thread.sleep(100);
//            }
//
//            // 요약 출력
//            printSeqSummary();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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
//    private void printSeqSummary() {
//        if (CustomJobListener2.sequentialJobCount > 0) {
//            System.out.println("\n=== 순차 처리 Summary ===");
//            System.out.println("순차 총 소요시간: " + CustomJobListener2.sequentialTotalTime + " ms");
//        } else {
//            System.out.println("\n순차 처리 작업이 수행되지 않았습니다.");
//        }
//    }
//
//}
