package com.soma.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YoutuberUpdateJobTestConfig {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("youtuberUpdateJob")
    private Job job;

    @Bean
    public JobLauncherTestUtils youtuberUpdateJobLauncherTestUtils() {
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJob(job);
        return jobLauncherTestUtils;
    }
}