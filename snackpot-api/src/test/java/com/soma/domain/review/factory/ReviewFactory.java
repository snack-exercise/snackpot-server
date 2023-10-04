package com.soma.domain.review.factory;

import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.member.entity.Member;
import com.soma.domain.review.entity.Review;

public class ReviewFactory {
    public static Review createReviewWithMemberAndExerciseAndContent(Member member, Exercise exercise, String content){
        return Review.builder()
                .member(member)
                .exercise(exercise)
                .content(content)
                .build();
    }
}
