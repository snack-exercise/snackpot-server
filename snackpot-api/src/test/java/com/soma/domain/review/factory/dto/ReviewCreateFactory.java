package com.soma.domain.review.factory.dto;

import com.soma.domain.rating.entity.RatingType;
import com.soma.domain.review.dto.request.ReviewCreateRequest;

public class ReviewCreateFactory {
    public static ReviewCreateRequest createReviewCreateRequest(Long exerciseId, RatingType ratingType, String reviewContent){
        return ReviewCreateRequest.builder()
                .exerciseId(exerciseId)
                .ratingType(ratingType)
                .reviewContent(reviewContent)
                .build();
    }
}
