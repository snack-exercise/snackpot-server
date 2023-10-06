package com.soma.domain.review.controller;

import com.soma.domain.review.dto.request.ReviewCreateRequest;
import com.soma.domain.review.service.ReviewService;
import com.soma.util.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

    @Operation(summary = "운동별 리뷰 목록 조회하기", description = "운동별 리뷰 목록을 조회합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @Parameter(name = "cursorId", description = "마지막으로 조회한 리뷰 ID, 맨 처음조회시에는 쿼리파라미터 작성 안해도 됩니다.", required = false,  in = ParameterIn.QUERY)
    @Parameter(name = "size", description = "가져올 개수, default 값은 5로 설정되어 있습니다.", required = false,  in = ParameterIn.QUERY)
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/exercises/{exerciseId}")
    public Response readAll(@PathVariable("exerciseId") Long exerciseId, @RequestParam(value = "size", defaultValue = "5", required = false) Integer size, @RequestParam(value = "cursorId", required = false) Long cursorId, @AuthenticationPrincipal UserDetails loginUser){
        return Response.success(reviewService.readAll(exerciseId, size, cursorId, loginUser.getUsername()));
    }

}
