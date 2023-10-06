package com.soma.domain.exercise.repository;

import com.soma.domain.bodypart.entity.BodyPartType;
import com.soma.domain.exercise.dto.request.ExerciseReadCondition;
import com.soma.domain.exercise.dto.response.ExerciseResponse;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ExerciseRepositoryCustom {
    Slice<ExerciseResponse> findAllByCondition(ExerciseReadCondition exerciseReadCondition, List<BodyPartType> bodyPartTypes, int size, String email);
}
