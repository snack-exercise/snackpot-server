package com.soma.service;

import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.entity.ExerciseType;
import com.soma.domain.exercise.entity.Level;
import com.soma.domain.youtuber.entity.Youtuber;
import com.soma.entity.exercise.repository.ExerciseRepository;
import com.soma.entity.youtuber.repository.YoutuberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
//@Sql(scripts = "/reset_batch_tables.sql")
@Slf4j
public class SchedulerBatchComparisonTest {
    @Autowired
    private YoutuberRepository youtuberRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private JobLauncherTestUtils exerciseUpdateJobLauncherTestUtils;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @PersistenceContext
    private EntityManager em;


    @BeforeEach
    public void setUp() {
        transactionTemplate.execute(status -> {
            Youtuber youtuber = youtuberRepository.save(new Youtuber("someChannelId", "someDescription", "someName", "someProfileImg"));
            List<Exercise> exercises = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                exercises.add(Exercise.builder()
                        .youtuber(youtuber)
                        .videoId("Jkb9BNBmJ04")
                        .calories(i)
                        .level(Level.EASY)
                        .effect("Effect " + i)
                        .title("Title " + i)
                        .thumbnail("Thumbnail URL " + i)
                        .timeSpent(i)
                        .exerciseType(ExerciseType.CARDIO)
                        .build());
            }
            exerciseRepository.saveAll(exercises);

            em.flush();
            em.clear();
            return null;
        });

    }

    @Test
    public void testScheduler() throws Exception {
        long schedulerStartTime = System.currentTimeMillis();
        schedulerService.updateExerciseInfo();
        long schedulerEndTime = System.currentTimeMillis();

        long schedulerDuration = schedulerEndTime - schedulerStartTime;

        log.info("Scheduler Duration: " + schedulerDuration + "ms");
    }

    @Test
    public void testBatch() throws Exception{ // 33m
        long batchStartTime = System.currentTimeMillis();
        exerciseUpdateJobLauncherTestUtils.launchJob(new JobParameters());
        long batchEndTime = System.currentTimeMillis();

        long batchDuration = batchEndTime - batchStartTime;

        log.info("Batch Duration: " + batchDuration + "ms");
    }
}
