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
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//@Configuration
//@EnableScheduling
//public class SchedulerConfig2 {   // 순차
//
//    private final JobLauncher jobLauncher;
//    private final List<Job> sequentialJobs;
//
//    public SchedulerConfig2(JobLauncher jobLauncher,
//                            Job statJob, Job billJob, Job afterJob) {
//        this.jobLauncher = jobLauncher;
//
//        // 순차 작업 목록
//        this.sequentialJobs = Arrays.asList(statJob, billJob, afterJob);
//    }
//
//    @Scheduled(cron = "*/5 * * * * *") // 매 5초마다 실행
//    public void runJobs() {
//        try {
//            // 순차 처리
//            System.out.println("\n[순차] 잡 실행 시작");
//            runSequentialJobs();
//
//            // 요약 출력
//            printSeqSummary();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void runSequentialJobs() throws InterruptedException {
//        for (Job job : sequentialJobs) {
//            JobExecution execution = runJob(job, "순차");
//            while (execution.isRunning()) {
//                Thread.sleep(100);
//            }
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
//        if (CustomJobListener.sequentialJobCount > 0) {
//            System.out.println("\n=== 순차 처리 Summary ===");
//            System.out.println("순차 총 소요시간: " + CustomJobListener.sequentialTotalTime + " ms");
//        } else {
//            System.out.println("\n순차 처리 작업이 수행되지 않았습니다.");
//        }
//    }
//
//}
