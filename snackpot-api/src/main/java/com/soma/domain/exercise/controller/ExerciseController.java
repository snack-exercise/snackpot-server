package com.soma.domain.exercise.controller;

import com.soma.domain.bodypart.entity.BodyPartType;
import com.soma.domain.exercise.dto.request.ExerciseFinishRequest;
import com.soma.domain.exercise.dto.request.ExerciseReadCondition;
import com.soma.domain.exercise.service.ExerciseService;
import com.soma.util.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/exercises")
@RestController
public class ExerciseController {
    private final ExerciseService exerciseService;

    @Operation(summary = "운동 목록 조회", description = "주어진 조건에 따라 운동 목록을 조회합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @Parameter(name = "cursorId", description = "마지막으로 조회한 운동 ID, 맨 처음조회시에는 쿼리파라미터 작성 안해도 됩니다.", required = false,  in = ParameterIn.QUERY)
    @Parameter(name = "like", description = "좋아요 조건입니다.", required = false,  in = ParameterIn.QUERY)
    @Parameter(name = "level", description = "난이도 조건입니다.", required = false,  in = ParameterIn.QUERY)
    @Parameter(name = "timeSpent", description = "시간 조건, 주어진 조건의 시간 이하인 운동을 조회합니다.", required = false,  in = ParameterIn.QUERY)
    @Parameter(name = "bodyPartTypes", description = "운동 부위 조건입니다.", required = false,  in = ParameterIn.QUERY)
    @Parameter(name = "size", description = "가져올 개수, default 값은 5로 설정되어 있습니다.", required = false,  in = ParameterIn.QUERY)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response readAllByCondition(
            @ModelAttribute ExerciseReadCondition condition,
            @RequestParam(value = "bodyPartTypes", required = false) List<BodyPartType> bodyPartTypes,
            @RequestParam(defaultValue = "5", value = "size") int size,
            @AuthenticationPrincipal UserDetails loginUser
    ) {
        if (loginUser != null) {
            return Response.success(exerciseService.readAllByCondition(condition, bodyPartTypes, size, loginUser.getUsername()));
        }else{
            return Response.success(exerciseService.readAllByCondition(condition, bodyPartTypes, size, null));
        }
    }

    @Operation(summary = "운동 종료", description = "운동 종료, 운동 기록을 생성합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @PostMapping("/finish")
    @ResponseStatus(HttpStatus.CREATED)
    public Response finish(@RequestBody ExerciseFinishRequest request, @AuthenticationPrincipal UserDetails loginUser) {
        exerciseService.finish(request, loginUser.getUsername());
        return Response.success();
    }


    @Operation(summary = "운동 상세 조회", description = "주어진 운동 id로 운동을 조회합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @Parameter(name = "exerciseId", description = "조회할 운동 id 입니다.", required = true,  in = ParameterIn.PATH)
    @GetMapping("/{exerciseId}")
    @ResponseStatus(HttpStatus.OK)
    public Response readExerciseDetails(@PathVariable Long exerciseId, @AuthenticationPrincipal UserDetails loginUser) {
        if (loginUser != null) {
            return Response.success(exerciseService.readExerciseDetails(exerciseId, loginUser.getUsername()));
        }else{
            return Response.success(exerciseService.readExerciseDetails(exerciseId, null));
        }
    }
}
