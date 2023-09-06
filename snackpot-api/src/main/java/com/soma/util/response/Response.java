package com.soma.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.soma.advice.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL) // null 값을 가지는 필드는 제외
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter // 응답 객체를 JSON으로 변환하기 위해 필요
public class Response {
    private Boolean success;
    private int code;
    private Result result;

    public static Response success() {
        return new Response(true, 0, null);
    }

    public static <T> Response success(T data) {
        return new Response(true, 0, new Success<>(data));
    }

    public static Response failure(ErrorCode errorCode) {
        return new Response(false, errorCode.getCode(), new Failure(errorCode.getMessage()));
    }
}