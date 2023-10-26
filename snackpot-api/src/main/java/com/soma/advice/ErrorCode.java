package com.soma.advice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /* Member */
    MEMBER_NOT_FOUND_EXCEPTION(-1100, "사용자가 존재하지 않습니다."),
    MEMBER_ALREADY_EXIST(-1101, "이미 해당 이름을 가진 사용자가 존재합니다."),

    /* Group */
    ALREADY_JOINED_GROUP_EXCEPTION(-1200, "이미 가입한 그룹입니다."),
    GROUP_NOT_FOUND_EXCEPTION(-1201, "그룹이 존재하지 않습니다."),

    /* Exercise */
    EXERCISE_NOT_FOUND_EXCEPTION(-1300, "운동이 존재하지 않습니다."),


    /* Notification */
    FCM_TOKEN_NOT_FOUND_EXCEPTION(-1400, "FCM 토큰을 찾을 수 없습니다.");
    private final int code;
    private final String message;
}
