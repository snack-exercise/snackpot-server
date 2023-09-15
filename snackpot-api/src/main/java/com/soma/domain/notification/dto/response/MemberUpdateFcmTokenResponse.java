package com.soma.domain.notification.dto.response;

import com.soma.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateFcmTokenResponse {
    private Long id;
    private String nickname;
    private String fcmToken;

    public static MemberUpdateFcmTokenResponse toDto(Member member) {
        return new MemberUpdateFcmTokenResponse(member.getId(), member.getName(), member.getFcmToken());
    }
}