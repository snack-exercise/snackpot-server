package com.soma.domain.review.service;

import com.soma.common.constant.Status;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.domain.rating.entity.Rating;
import com.soma.domain.rating.repository.RatingRepository;
import com.soma.domain.review.dto.request.ReviewCreateRequest;
import com.soma.domain.review.dto.response.ReviewListReponse;
import com.soma.domain.review.dto.response.ReviewResponse;
import com.soma.domain.review.entity.Review;
import com.soma.domain.review.repository.ReviewRepository;
import com.soma.exception.exercise.ExerciseNotFoundException;
import com.soma.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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

    @Transactional
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

    public ReviewListReponse readAll(Long exerciseId, Integer size, Long cursorId, String username) {
        if(cursorId == null){ // 처음 리뷰 목록 조회 요청인 경우 cursorId가 없음
            Slice<Review> firstReviewSlice = reviewRepository.getFirstReviewSlice(exerciseId, PageRequest.of(0, size));
            return ReviewSlicetoReviewListReponse(firstReviewSlice);
        }
        Slice<Review> reviewSliceByCursorId = reviewRepository.getReviewSliceByCursorId(exerciseId, cursorId, PageRequest.of(0, size));
        return ReviewSlicetoReviewListReponse(reviewSliceByCursorId);
    }

    private ReviewListReponse ReviewSlicetoReviewListReponse(Slice<Review> reviewSlice){
        return ReviewListReponse.toDto(reviewSlice.getNumberOfElements(),
                new SliceImpl<>(reviewSlice.getContent().stream().map(review -> ReviewResponse.toDto(review, review.getMember())).toList(), reviewSlice.getPageable(), reviewSlice.hasNext()));
    }
}
