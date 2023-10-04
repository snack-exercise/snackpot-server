package com.soma.domain.member.service;

import com.soma.common.constant.Status;
import com.soma.domain.member.dto.response.MemberTotalNumResponse;
import com.soma.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberTotalNumResponse getTotalNum() {
        return MemberTotalNumResponse.toDto(memberRepository.countByStatus(Status.ACTIVE));
    }
}
