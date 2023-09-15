package com.soma.domain.member.factory.fixtures;

import com.soma.domain.member.entity.RoleType;

import static com.soma.domain.member.entity.RoleType.*;

public class MemberFixtures {
    public static String 이름 = "김누구";
    public static Integer 하루목표운동시간 = 10;
    public static String 이메일 = "test@naver.com";
    public static String 프로필사진 = "profile_image";
    public static String 비밀번호 = "password";
    public static RoleType 일반권한 = USER;
    public static RoleType 게스트권한 = GUEST;
    public static String FCM토큰 = "sample_fcm_token";

}
