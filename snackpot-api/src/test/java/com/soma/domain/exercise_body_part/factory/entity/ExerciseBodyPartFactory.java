package com.soma.domain.exercise_body_part.factory.entity;

import com.soma.domain.bodypart.entity.BodyPart;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise_bodypart.entity.ExerciseBodyPart;

public class ExerciseBodyPartFactory {
    public static ExerciseBodyPart createExerciseBodyPart(Exercise exercise, BodyPart bodyPart) {
        return ExerciseBodyPart.builder()
                .exercise(exercise)
                .bodyPart(bodyPart).build();
    }
}
