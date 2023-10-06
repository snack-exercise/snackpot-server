package com.soma.domain.exercise_like.factory.entity;

import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise_like.entity.ExerciseLike;
import com.soma.domain.member.entity.Member;

public class ExerciseLikeFactory {
    public static ExerciseLike createExerciseLike(Member member, Exercise exercise) {
        return ExerciseLike.builder()
                .member(member)
                .exercise(exercise)
                .build();
    }
}
