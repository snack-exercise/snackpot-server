package com.soma.auth.service;


import com.soma.auth.dto.request.SignInRequest;
import com.soma.auth.dto.request.SignUpRequest;
import com.soma.auth.dto.response.SignInResponse;
import com.soma.auth.dto.response.SignUpResponse;
import com.soma.common.constant.Status;
import com.soma.exception.MemberNicknameAlreadyExistsException;
import com.soma.exception.MemberNotFoundException;
import com.soma.member.entity.Member;
import com.soma.member.repository.MemberRepository;
import com.soma.notification.service.NotificationService;
import com.soma.security.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final NotificationService notificationService;

    @Transactional
    public SignUpResponse signup(SignUpRequest request) {
        String email = UUID.randomUUID() + "@socialUser.com";

        validateDuplicateNickname(request);

        Member member = Member.builder()
                .email(email)
                .name(request.getName())
                .dailyGoalTime(request.getDailyGoalTime())
                .build();
        // fcm 토큰 저장
        member.updateFcmToken(request.getFcmToken());

        memberRepository.save(member);

        String accessToken = jwtService.createAccessToken(email);

        return new SignUpResponse("Bearer " + accessToken, member.getId());
    }

    @Transactional
    public SignInResponse login(SignInRequest request) {
        Member member = memberRepository.findByNameAndStatus(request.getName(), Status.ACTIVE).orElseThrow(MemberNotFoundException::new);

        String accessToken = jwtService.createAccessToken(member.getEmail());

        // fcm 토큰 저장
        member.updateFcmToken(request.getFcmToken());

        return new SignInResponse("Bearer " + accessToken, member.getId());
    }

    private void validateDuplicateNickname(SignUpRequest request) {
        if (memberRepository.existsByName(request.getName())) {
            throw new MemberNicknameAlreadyExistsException();
        }
    }
}