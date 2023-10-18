package com.soma.domain.review.dto.request;

import com.soma.domain.rating.entity.RatingType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewCreateRequest {
    @NotNull(message = "운동 id는 필수입니다.")
    private Long exerciseId;
    private RatingType ratingType;
    @Size(max = 200, message = "리뷰 내용은 200자 이하이어야 합니다.")
    private String reviewContent;

    @Builder
    public ReviewCreateRequest(Long exerciseId, RatingType ratingType, String reviewContent) {
        this.exerciseId = exerciseId;
        this.ratingType = ratingType;
        this.reviewContent = reviewContent;
    }
}
