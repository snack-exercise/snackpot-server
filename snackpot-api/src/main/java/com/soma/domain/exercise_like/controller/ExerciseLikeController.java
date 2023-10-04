package com.soma.domain.exercise_like.controller;

import com.soma.domain.exercise_like.service.ExerciseLikeService;
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
@Tag(name = "ExerciseLike", description = "운동 좋아요 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/exercises/{exerciseId}/likes")
public class ExerciseLikeController {
    private final ExerciseLikeService exerciseLikeService;

    @Operation(summary = "운동 좋아요", description = "운동 좋아요를 생성합니다.", security = {@SecurityRequirement(name = "Authorization")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Response create(@PathVariable Long exerciseId, @AuthenticationPrincipal UserDetails loginUser) {
        exerciseLikeService.create(exerciseId, loginUser.getUsername());
        return Response.success();
    }

    @Operation(summary = "운동 좋아요 취소", description = "운동 좋아요를 취소합니다.", security = {@SecurityRequirement(name = "Authorization")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public Response delete(@PathVariable Long exerciseId, @AuthenticationPrincipal UserDetails loginUser) {
        exerciseLikeService.delete(exerciseId, loginUser.getUsername());
        return Response.success();
    }
}
