package com.soma.domain.exercise_record.entity;

import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class ExerciseRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Exercise exercise;

    @Builder
    public ExerciseRecord(Member member, Exercise exercise) {
        this.member = member;
        this.exercise = exercise;
    }
}
