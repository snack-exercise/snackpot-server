package com.soma.domain.review.dto.request;

import com.soma.domain.rating.entity.RatingType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewCreateRequest {
    private Long exerciseId;
    private RatingType ratingType;
    private String reviewContent;

    @Builder
    public ReviewCreateRequest(Long exerciseId, RatingType ratingType, String reviewContent) {
        this.exerciseId = exerciseId;
        this.ratingType = ratingType;
        this.reviewContent = reviewContent;
    }
}
