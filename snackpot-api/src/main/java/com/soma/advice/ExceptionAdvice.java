package com.soma.advice;

import com.soma.exception.MemberNotFoundException;
import com.soma.util.response.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.soma.advice.ErrorCode.MEMBER_NOT_FOUND_EXCEPTION;

@Getter
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response MemberNotFoundExceptionHandler(MemberNotFoundException e){
//        Sentry.captureException(e);
        return Response.failure(MEMBER_NOT_FOUND_EXCEPTION);
    }

}
