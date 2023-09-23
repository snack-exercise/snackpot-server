package com.soma.domain.exercise.factory.dto;

import com.soma.domain.exercise.dto.request.ExerciseFinishRequest;

public class ExerciseFinishFactory {
    public static ExerciseFinishRequest createExerciseFinishRequest(Long exerciseId) {
        return new ExerciseFinishRequest(exerciseId);
    }
}
