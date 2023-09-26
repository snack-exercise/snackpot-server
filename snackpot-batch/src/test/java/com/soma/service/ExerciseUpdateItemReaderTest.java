package com.soma.service;

import com.soma.domain.exercise.entity.Exercise;
import com.soma.service.reader.ExerciseUpdateItemReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ExerciseUpdateItemReaderTest {
    @Autowired
    private ExerciseUpdateItemReader exerciseUpdateItemReader;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    void read() throws Exception {
        Exercise exercise = Exercise.builder().videoId("O2HksjLfPAU").build();

        entityManager.persist(exercise);
        entityManager.flush();

        Exercise result = exerciseUpdateItemReader.read();
    }
}