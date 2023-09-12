package com.soma.advice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /* Member */
    MEMBER_NOT_FOUND_EXCEPTION(-1100, "사용자가 존재하지 않습니다.");

    private final int code;
    private final String message;
}
