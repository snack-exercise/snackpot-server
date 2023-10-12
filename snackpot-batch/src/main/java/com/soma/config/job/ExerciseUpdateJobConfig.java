package com.soma.config.job;

import com.soma.service.reader.ExerciseUpdateItemReader;
import com.soma.service.writer.ExerciseUpdateItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ExerciseUpdateJobConfig extends DefaultBatchConfiguration {

    private final ExerciseUpdateItemReader exerciseUpdateItemReader;

    private final ExerciseUpdateItemWriter exerciseUpdateItemWriter;


    @Bean
    public Job exerciseUpdateJob(JobRepository jobRepository, @Qualifier("exerciseUpdateStep") Step step) {
        return new JobBuilder("exerciseUpdateJob", jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public Step exerciseUpdateStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        int chunkSize = 100;
        return new StepBuilder("exerciseUpdateStep", jobRepository)
                .chunk(chunkSize, platformTransactionManager)
                .reader(exerciseUpdateItemReader)
                .writer(exerciseUpdateItemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor()) // 멀티 스레드로 병렬 처리
                .transactionManager(platformTransactionManager)
                .build();
    }
}
