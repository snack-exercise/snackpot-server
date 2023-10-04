package com.soma.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberTotalNumResponse {
    private Long totalNum;

    public static MemberTotalNumResponse toDto(Long totalNum) {
        return new MemberTotalNumResponse(totalNum);
    }
}
