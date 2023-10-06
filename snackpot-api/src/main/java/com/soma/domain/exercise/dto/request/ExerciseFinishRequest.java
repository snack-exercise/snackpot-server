package com.soma.domain.exercise.dto.request;

import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise_record.entity.ExerciseRecord;
import com.soma.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExerciseFinishRequest {
    private Long exerciseId;

    public static ExerciseRecord toEntity(Exercise exercise, Member member) {
        return ExerciseRecord.builder()
                .member(member)
                .exercise(exercise).build();
    }
}
