package com.soma.member.factory.entity;

import com.soma.domain.member.entity.Member;

import static com.soma.domain.member.factory.fixtures.MemberFixtures.*;

public class MemberFactory {

    public static Member createUserRoleMember(){
        return Member.builder()
                .name(이름)
                .dailyGoalTime(하루목표운동시간)
                .email(이메일)
                .profileImg(프로필사진)
                .password(비밀번호)
                .roleType(일반권한)
                .fcmToken(FCM토큰)
                .build();
    }

    public static Member createUserRoleMemberWithNameAndEmail(String name, String email){
        return Member.builder()
                .name(name)
                .dailyGoalTime(하루목표운동시간)
                .email(email)
                .profileImg(프로필사진)
                .password(비밀번호)
                .roleType(일반권한)
                .fcmToken(FCM토큰)
                .build();
    }
}