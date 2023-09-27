package com.soma.domain.review.service;

import com.soma.common.constant.Status;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.domain.rating.entity.Rating;
import com.soma.domain.rating.repository.RatingRepository;
import com.soma.domain.review.dto.request.ReviewCreateRequest;
import com.soma.domain.review.entity.Review;
import com.soma.domain.review.repository.ReviewRepository;
import com.soma.exception.exercise.ExerciseNotFoundException;
import com.soma.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;
    private final MemberRepository memberRepository;
    private final ExerciseRepository exerciseRepository;

    public void create(ReviewCreateRequest request, String email) {
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(MemberNotFoundException::new);
        Exercise exercise = exerciseRepository.findById(request.getExerciseId()).orElseThrow(ExerciseNotFoundException::new);

        if(request.getRatingType() != null){
            ratingRepository.save(Rating.builder()
                    .ratingType(request.getRatingType())
                    .exercise(exercise)
                    .member(member)
                    .build());
        }

        if(request.getReviewContent() != null){
            reviewRepository.save(Review.builder()
                    .content(request.getReviewContent())
                    .exercise(exercise)
                    .member(member)
                    .build());
        }
    }
}
