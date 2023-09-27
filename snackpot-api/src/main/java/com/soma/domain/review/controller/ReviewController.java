package com.soma.domain.review.controller;

import com.soma.domain.review.dto.request.ReviewCreateRequest;
import com.soma.domain.review.service.ReviewService;
import com.soma.util.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Review", description = "리뷰 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰와 평가 생성", description = "하나의 리뷰와 평가를 생성합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Response create(@RequestBody ReviewCreateRequest request, @AuthenticationPrincipal UserDetails loginUser){
        reviewService.create(request, loginUser.getUsername());
        return Response.success();
    }

}
