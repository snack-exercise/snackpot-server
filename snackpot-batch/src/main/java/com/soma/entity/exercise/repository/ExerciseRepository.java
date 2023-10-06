package com.soma.entity.exercise.repository;

import com.soma.common.constant.Status;
import com.soma.domain.exercise.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAllByStatus(Status status);
}
