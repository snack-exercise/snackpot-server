package com.soma.domain.exercise_bodypart.repository;

import com.soma.common.constant.Status;
import com.soma.domain.exercise_bodypart.entity.ExerciseBodyPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseBodypartRepository extends JpaRepository<ExerciseBodyPart, Long> {
    List<ExerciseBodyPart> findAllByExerciseIdAndStatus(Long exerciseId, Status status);

}

