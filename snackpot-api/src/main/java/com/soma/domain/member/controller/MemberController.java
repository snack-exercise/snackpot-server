package com.soma.domain.member.controller;

import com.soma.domain.member.service.MemberService;
import com.soma.util.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public Response getTotalNum() {
        return Response.success(memberService.getTotalNum());
    }
}
