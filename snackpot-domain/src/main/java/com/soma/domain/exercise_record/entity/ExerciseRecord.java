package com.soma.domain.exercise_record.entity;

import com.soma.common.BaseEntity;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class ExerciseRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Exercise exercise;

    private Integer time;

    @Builder
    public ExerciseRecord(Member member, Exercise exercise, Integer time) {
        this.member = member;
        this.exercise = exercise;
        this.time = time;
    }
}
