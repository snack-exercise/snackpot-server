package com.soma.domain.group.controller;

import com.soma.domain.group.dto.request.GroupCreateRequest;
import com.soma.domain.group.dto.request.GroupJoinRequest;
import com.soma.domain.group.service.GroupService;
import com.soma.util.response.Response;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Exgroup", description = "운동 그룹 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    @Timed("CreateGroupTimer")
    @Operation(summary = "운동 그룹 생성", description = "하나의 운동 그룹을 생성합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Response create(@Valid @RequestBody GroupCreateRequest request, @AuthenticationPrincipal UserDetails loginUser){
        return Response.success(groupService.create(request, loginUser.getUsername()));
    }

    @Timed("JoinGroupTimer")
    @Operation(summary = "그룹 가입", description = "코드로 그룹을 가입합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/join")
    public Response join(@Valid @RequestBody GroupJoinRequest request, @AuthenticationPrincipal UserDetails loginUser){
        return Response.success(groupService.join(request, loginUser.getUsername()));
    }

    @Timed("ReadAllJoinGroupTimer")
    @Operation(summary = "가입되어있는 모든 그룹 조회", description = "가입된 모든 그룹을 조회합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @Parameter(name = "groupIdCursor", description = "마지막으로 조회한 그룹 ID, 맨 처음조회시에는 쿼리파라미터 작성 안해도 됩니다.", required = false,  in = ParameterIn.QUERY)
    @Parameter(name = "size", description = "가져올 개수, default 값은 5로 설정되어 있습니다.", required = false,  in = ParameterIn.QUERY)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public Response readAll(@RequestParam(required = false, value = "groupIdCursor") Long groupIdCursor, @RequestParam(defaultValue = "5", value = "size") Integer size, @AuthenticationPrincipal UserDetails loginUser){
        return Response.success(groupService.readAll(groupIdCursor, size, loginUser.getUsername()));
    }

    @Timed("ReadAllAbsenteesTimer")
    @Operation(summary = "오늘 운동 미수행 사용자 목록 조회", description = "오늘 운동 미수행 사용자 목록을 조회합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @Parameter(name = "groupId", description = "운동 그룹 ID",  required = true,  in = ParameterIn.PATH)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{groupId}/absentees")
    public Response readAllAbsentees(@PathVariable(value = "groupId") Long groupId, @AuthenticationPrincipal UserDetails loginUser){
        return Response.success(groupService.readAllAbsentees(groupId));
    }

    @Timed("ReadExerciseTimeStaticsTimer")
    @Operation(summary = "그룹 일주일 운동 시간 통계 조회", description = "그룹 일주일 운동 시간 통계를 조회합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @Parameter(name = "groupId", description = "운동 그룹 ID",  required = true,  in = ParameterIn.PATH)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{groupId}/statistics")
    public Response readExerciseTimeStatics(@PathVariable(value = "groupId") Long groupId, @AuthenticationPrincipal UserDetails loginUser){
        return Response.success(groupService.readExerciseTimeStatics(groupId));
    }

    @Timed("DeleteGroupTimer")
    @Operation(summary = "그룹 삭제", description = "그룹을 삭제합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @Parameter(name = "groupId", description = "운동 그룹 ID",  required = true,  in = ParameterIn.PATH)
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{groupId}")
    public Response delete(@PathVariable(value = "groupId") Long groupId, @AuthenticationPrincipal UserDetails loginUser) {
        groupService.delete(groupId);
        return Response.success();
    }

    @Timed("ReadExerciseCheckListTimer")
    @Operation(summary = "그룹 일주일 운동 수행 결과표 조회", description = "그룹 일주일 운동 수행 결과표를 조회합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @Parameter(name = "groupId", description = "운동 그룹 ID",  required = true,  in = ParameterIn.PATH)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{groupId}/checklist")
    public Response readExerciseCheckList(@PathVariable(value = "groupId") Long groupId, @AuthenticationPrincipal UserDetails loginUser){
        return Response.success(groupService.readExerciseCheckList(groupId));
    }
}



