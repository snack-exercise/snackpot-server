package com.soma.config.job;

import com.soma.domain.youtuber.entity.Youtuber;
import com.soma.entity.youtuber.repository.YoutuberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.batch.core.BatchStatus.COMPLETED;

@SpringBootTest
@Sql(scripts = "/reset_batch_tables.sql") // 이전에 완료된 상태의 step을 다시 실행하지 않기 때문에 Job 실행기록을 저장하는 Repository 초기화
class YoutuberUpdateJobConfigTest {
    @Autowired
    private YoutuberRepository youtuberRepository;

    @Autowired
    private JobLauncherTestUtils youtuberUpdateJobLauncherTestUtils;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @AfterEach
    public void tearDown() {
        youtuberRepository.deleteAll();
    }

    @Test
    public void testExerciseUpdateJob() throws Exception {
        // given
        Youtuber youtuber = Youtuber.builder().channelId("UCDunp3yW9Yp6cWcOv3wn-cg").build();

        transactionTemplate.execute(status -> {
            youtuberRepository.save(youtuber);
            return null;
        });

        // when
        JobExecution jobExecution = youtuberUpdateJobLauncherTestUtils.launchJob(new JobParameters());

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(COMPLETED);
        List<Youtuber> youtubers = youtuberRepository.findAll();
        assertThat(youtubers).hasSize(1);
        assertThat(youtubers.get(0).getProfileImg()).isNotNull();
        System.out.println(youtubers.get(0).getProfileImg());
    }

}