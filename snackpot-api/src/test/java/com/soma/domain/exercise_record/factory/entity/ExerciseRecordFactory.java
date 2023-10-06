package com.soma.domain.exercise_record.factory.entity;

import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise_record.entity.ExerciseRecord;
import com.soma.domain.member.entity.Member;

public class ExerciseRecordFactory {
    public static ExerciseRecord createExerciseRecordWithExerciseAndMember(Exercise exercise, Member member){
        return ExerciseRecord.builder()
                .exercise(exercise)
                .member(member)
                .time(10)
                .build();
    }

    public static ExerciseRecord createExerciseRecordWithExerciseAndMemberAndTime(Exercise exercise, Member member, Integer time){
        return ExerciseRecord.builder()
                .exercise(exercise)
                .member(member)
                .time(time)
                .build();
    }
}
