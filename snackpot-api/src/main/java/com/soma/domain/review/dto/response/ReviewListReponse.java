package com.soma.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@AllArgsConstructor
@Getter
public class ReviewListReponse {
    private Integer totalReviewNum;
    private Slice<ReviewResponse> reviews;


    public static ReviewListReponse toDto(Integer totalReviewNum, Slice<ReviewResponse> reviews){
        return new ReviewListReponse(totalReviewNum, reviews);
    }
}
