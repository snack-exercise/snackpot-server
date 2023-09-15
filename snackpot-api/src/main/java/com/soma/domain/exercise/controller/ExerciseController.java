package com.soma.domain.exercise.controller;

import com.soma.domain.exercise.dto.request.ExerciseFinishRequest;
import com.soma.domain.exercise.dto.request.ExerciseReadCondition;
import com.soma.domain.exercise.service.ExerciseService;
import com.soma.util.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/exercises")
@RestController
public class ExerciseController {
    private final ExerciseService exerciseService;

    @GetMapping
    public Response readAllByCondition(
            ExerciseReadCondition condition,
            int size,
            @AuthenticationPrincipal UserDetails loginUser
    ) {
        return Response.success(exerciseService.readAllByCondition(condition, size, loginUser.getUsername()));
    }

    @PostMapping("/finish")
    public Response finish(@RequestBody ExerciseFinishRequest request, @AuthenticationPrincipal UserDetails loginUser) {
        exerciseService.finish(request, loginUser.getUsername());
        return Response.success();
    }
}
