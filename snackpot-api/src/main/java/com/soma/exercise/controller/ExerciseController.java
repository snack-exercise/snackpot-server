package com.soma.exercise.controller;

import com.soma.exercise.dto.request.ExerciseReadCondition;
import com.soma.exercise.service.ExerciseService;
import com.soma.util.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
