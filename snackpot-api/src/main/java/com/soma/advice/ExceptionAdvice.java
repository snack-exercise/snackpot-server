package com.soma.advice;

import com.soma.exception.group.AlreadyJoinedGroupException;
import com.soma.exception.group.GroupNotFoundException;
import com.soma.exception.member.MemberNotFoundException;
import com.soma.util.response.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.soma.advice.ErrorCode.*;

@Getter
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    /* Group */
    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundExceptionHandler(MemberNotFoundException e){
//        Sentry.captureException(e);
        return Response.failure(MEMBER_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(GroupNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response groupNotFoundExceptionHandler(GroupNotFoundException e){
//        Sentry.captureException(e);
        return Response.failure(GROUP_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(AlreadyJoinedGroupException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response alreadyJoinedGroupExceptionHandler(AlreadyJoinedGroupException e){
//        Sentry.captureException(e);
        return Response.failure(ALREADY_JOINED_GROUP_EXCEPTION);
    }


}
