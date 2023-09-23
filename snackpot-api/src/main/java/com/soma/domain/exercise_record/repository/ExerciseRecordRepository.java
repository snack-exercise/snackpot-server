package com.soma.domain.exercise_record.repository;

import com.soma.domain.exercise_record.entity.ExerciseRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRecordRepository extends JpaRepository<ExerciseRecord, Long> {
}
