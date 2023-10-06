package com.soma.config.job;

import com.soma.domain.exercise.entity.Exercise;
import com.soma.entity.exercise.repository.ExerciseRepository;
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
class ExerciseUpdateJobConfigTest {
    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private JobLauncherTestUtils exerciseUpdateJobLauncherTestUtils;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @AfterEach
    public void tearDown() {
        exerciseRepository.deleteAll();
    }

    @Test
    public void testExerciseUpdateJob() throws Exception {
        // given
        Exercise exercise = Exercise.builder().videoId("O2HksjLfPAU").build();

        transactionTemplate.execute(status -> {
            exerciseRepository.save(exercise);
            return null;
        });

        // when
        JobExecution jobExecution = exerciseUpdateJobLauncherTestUtils.launchJob(new JobParameters());


        // then
        assertThat(jobExecution.getStatus()).isEqualTo(COMPLETED);

        List<Exercise> exercises = exerciseRepository.findAll();
        assertThat(exercises).hasSize(1);
        assertThat(exercises.get(0).getThumbnail()).isNotNull();
        System.out.println(exercises.get(0).getThumbnail());
    }
}