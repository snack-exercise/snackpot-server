package com.soma.domain.exercise.repository;

import com.soma.domain.exercise.dto.request.ExerciseReadCondition;
import com.soma.domain.exercise.dto.response.ExerciseResponse;
import org.springframework.data.domain.Slice;

public interface ExerciseRepositoryCustom {
    Slice<ExerciseResponse> findAllByCondition(ExerciseReadCondition exerciseReadCondition, int size, String email);
}
