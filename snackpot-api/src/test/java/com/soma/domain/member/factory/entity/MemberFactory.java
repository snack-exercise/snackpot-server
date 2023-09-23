package com.soma.domain.member.factory.entity;

import com.soma.domain.member.entity.Member;
import com.soma.domain.member.factory.fixtures.MemberFixtures;

public class MemberFactory {

    public static Member createUserRoleMember(){
        return Member.builder()
                .name(MemberFixtures.이름)
                .dailyGoalTime(MemberFixtures.하루목표운동시간)
                .email(MemberFixtures.이메일)
                .profileImg(MemberFixtures.프로필사진)
                .password(MemberFixtures.비밀번호)
                .roleType(MemberFixtures.일반권한)
                .fcmToken(MemberFixtures.FCM토큰)
                .build();
    }
}