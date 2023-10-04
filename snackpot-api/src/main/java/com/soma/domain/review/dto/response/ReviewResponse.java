package com.soma.domain.review.dto.response;

import com.soma.domain.member.entity.Member;
import com.soma.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
public class ReviewResponse {
    private Long reviewId;
    private String content;
    private String userName;
    private String userProfileImg;
    private LocalDate createdAt;

    public static ReviewResponse toDto(Review review, Member member){
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .userName(member.getName())
                .userProfileImg(member.getProfileImg())
                .createdAt(review.getCreatedAt().toLocalDate())
                .build();
    }
}
