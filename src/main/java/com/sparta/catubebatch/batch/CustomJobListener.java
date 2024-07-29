package com.sparta.catubebatch.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class CustomJobListener implements JobExecutionListener {

    public static long sequentialTotalTime = 0;
    public static int sequentialJobCount = 0;

    public static long parallelStartTime = 0;
    public static long parallelEndTime = 0;

    private static final Object LOCK = new Object();
    private static int runningParallelJobs = 0;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        jobExecution.getExecutionContext().putLong("startTime", System.currentTimeMillis());

        String mode = jobExecution.getJobParameters().getString("mode", "순차");
        if ("병렬".equals(mode)) {
            synchronized (LOCK) {
                runningParallelJobs++;
                if (runningParallelJobs == 1) {
                    parallelStartTime = System.currentTimeMillis();
                }
            }
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long startTime = jobExecution.getExecutionContext().getLong("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        String mode = jobExecution.getJobParameters().getString("mode", "순차");

        System.out.println("[" + mode + " Job] " + jobExecution.getJobInstance().getJobName() + " completed in " + duration + " ms");

        if ("순차".equals(mode)) {
            synchronized (LOCK) {
                sequentialTotalTime += duration;
                sequentialJobCount++;
            }
        }
        else if ("병렬".equals(mode)) {
            synchronized (LOCK) {
                runningParallelJobs--;
                System.out.println("[" + jobExecution.getJobInstance().getJobName() + "] Job finished.");
                if (runningParallelJobs == 0) {
                    parallelEndTime = endTime;
                    System.out.println("병렬 잡 종료 시간 기록: " + parallelEndTime + " ms");
                }
            }
        }
    }
}
