package com.sparta.catubebatch.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class CustomJobListener2 implements JobExecutionListener {

    public static long sequentialTotalTime = 0;
    public static int sequentialJobCount = 0;

    public static long parallelStartTime = 0;
    public static long parallelEndTime = 0;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        jobExecution.getExecutionContext().putLong("startTime", System.currentTimeMillis());

        String mode = jobExecution.getJobParameters().getString("mode", "순차");
        if ("병렬".equals(mode)) {
            synchronized (CustomJobListener.class) {
                if (parallelStartTime == 0) {
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
            synchronized (CustomJobListener.class) {
                sequentialTotalTime += duration;
                sequentialJobCount++;
            }
        } else if ("병렬".equals(mode)) {
            synchronized (CustomJobListener.class) {
                parallelEndTime = Math.max(parallelEndTime, endTime);
            }
        }
    }
}
