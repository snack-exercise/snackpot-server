package com.soma.domain.exercise.repository;

import com.soma.common.constant.Status;
import com.soma.domain.exercise.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long>, ExerciseRepositoryCustom {
    Optional<Exercise> findByIdAndStatus(Long id, Status status);
}
