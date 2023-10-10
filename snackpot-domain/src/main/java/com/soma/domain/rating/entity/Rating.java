package com.soma.domain.rating.entity;

import com.soma.common.BaseEntity;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Rating extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exerciseId")
    private Exercise exercise;

    @Enumerated(EnumType.STRING)
    private RatingType ratingType;

    @Builder
    public Rating(Member member, Exercise exercise, RatingType ratingType) {
        this.member = member;
        this.exercise = exercise;
        this.ratingType = ratingType;
        active();
    }
}
