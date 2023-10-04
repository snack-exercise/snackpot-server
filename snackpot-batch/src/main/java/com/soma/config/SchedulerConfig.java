package com.soma.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerConfig {

    private final JobLauncher jobLauncher;

    private final Job exerciseUpdateJob;

    private final Job youtuberUpdateJob;


    @Scheduled(cron = "0 0 1 * * ?")
    public void perform() throws JobExecutionException {
        JobParameters params = new JobParametersBuilder()
                .addDate("jobDate", new Date())
                .toJobParameters();

        jobLauncher.run(exerciseUpdateJob, params);
        jobLauncher.run(youtuberUpdateJob, params);
    }
}