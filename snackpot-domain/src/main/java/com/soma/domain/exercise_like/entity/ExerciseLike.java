package com.soma.domain.exercise_like.entity;

import com.soma.common.BaseTimeEntity;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ExerciseLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exerciseId")
    private Exercise exercise;

    @Builder
    public ExerciseLike(Member member, Exercise exercise) {
        this.member = member;
        this.exercise = exercise;
    }
}
