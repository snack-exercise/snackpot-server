package com.soma.domain.review.entity;

import com.soma.common.BaseEntity;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Exercise exercise;

    private String content;

    @Builder
    public Review(Member member, Exercise exercise, String content) {
        this.member = member;
        this.exercise = exercise;
        this.content = content;
        active();
    }
}
