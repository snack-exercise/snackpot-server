package com.soma.domain.member.controller;

import com.soma.domain.member.dto.request.UpdateDailyGoalTimeRequest;
import com.soma.domain.member.service.MemberService;
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
@Tag(name = "Member", description = "멤버 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "총 사용자 수 조회", description = "총 사용자 수를 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/total-num")
    public Response readTotalNum() {
        return Response.success(memberService.readTotalNum());
    }

    @Operation(summary = "마이페이지 조회", description = "마이페이지를 조회합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/my")
    public Response readMyInfo(@AuthenticationPrincipal UserDetails loginUser) {
        return Response.success(memberService.readMyInfo(loginUser.getUsername()));
    }

    @Operation(summary = "운동 목표 시간 수정", description = "운동 목표 시간을 수정합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/dailyGoalTime")
    public Response updateDailyGoalTime(@RequestBody UpdateDailyGoalTimeRequest request, @AuthenticationPrincipal UserDetails loginUser) {
        memberService.updateDailyGoalTime(request, loginUser.getUsername());
        return Response.success();
    }

    @Operation(summary = "회원 탈퇴", description = "회원을 탈퇴합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("")
    public Response delete(@AuthenticationPrincipal UserDetails loginUser) {
        memberService.delete(loginUser.getUsername());
        return Response.success();
    }
}
