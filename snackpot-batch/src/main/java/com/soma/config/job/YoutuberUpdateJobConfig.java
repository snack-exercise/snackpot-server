package com.soma.config.job;

import com.soma.service.reader.YoutuberUpdateItemReader;
import com.soma.service.writer.YoutuberUpdateItemWriter;
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
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class YoutuberUpdateJobConfig extends DefaultBatchConfiguration {

    private final YoutuberUpdateItemReader youtuberUpdateItemReader;

    private final YoutuberUpdateItemWriter youtuberUpdateItemWriter;

    @Bean
    public Job youtuberUpdateJob(JobRepository jobRepository, @Qualifier("youtuberUpdateStep") Step step) {
        return new JobBuilder("youtuberUpdateJob", jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public Step youtuberUpdateStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        int chunkSize = 10;
        return new StepBuilder("youtuberUpdateStep", jobRepository)
                .chunk(chunkSize, platformTransactionManager)
                .reader(youtuberUpdateItemReader)
                .writer(youtuberUpdateItemWriter)
                .transactionManager(platformTransactionManager)
                .build();
    }
}
