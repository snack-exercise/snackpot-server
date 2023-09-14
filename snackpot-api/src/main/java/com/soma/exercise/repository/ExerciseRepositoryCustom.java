package com.soma.exercise.repository;

import com.soma.exercise.dto.request.ExerciseReadCondition;
import com.soma.exercise.dto.response.ExerciseResponse;
import org.springframework.data.domain.Slice;

public interface ExerciseRepositoryCustom {
    Slice<ExerciseResponse> findAllByCondition(ExerciseReadCondition exerciseReadCondition, int size, String email);
}
