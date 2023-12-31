package com.soma.advice;

import com.soma.exception.exercise.ExerciseNotFoundException;
import com.soma.exception.group.AlreadyJoinedGroupException;
import com.soma.exception.group.GroupNotFoundException;
import com.soma.exception.member.ExceedDailyExerciseGoalException;
import com.soma.exception.member.FCMTokenNotFoundException;
import com.soma.exception.member.MemberNicknameAlreadyExistsException;
import com.soma.exception.member.MemberNotFoundException;
import com.soma.util.response.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.soma.advice.ErrorCode.*;

@Getter
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    /* Member */
    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundExceptionHandler(MemberNotFoundException e){
        return Response.failure(MEMBER_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(ExceedDailyExerciseGoalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response memberNotFoundExceptionHandler(ExceedDailyExerciseGoalException e){
        return Response.failure(EXCEED_DAILY_EXERCISE_GOAL__EXCEPTION);
    }

    /* Group */
    @ExceptionHandler(GroupNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response groupNotFoundExceptionHandler(GroupNotFoundException e){
        return Response.failure(GROUP_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(AlreadyJoinedGroupException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response alreadyJoinedGroupExceptionHandler(AlreadyJoinedGroupException e){
        return Response.failure(ALREADY_JOINED_GROUP_EXCEPTION);
    }

    /* Auth */
    @ExceptionHandler(MemberNicknameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response memberNicknameAlreadyExistsExceptionHandler(MemberNicknameAlreadyExistsException e){
        return Response.failure(MEMBER_ALREADY_EXIST);
    }

    /* Exercise */
    @ExceptionHandler(ExerciseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response exerciseNotFoundExceptionHandler(ExerciseNotFoundException e){
        log.error(EXERCISE_NOT_FOUND_EXCEPTION.getMessage());
        return Response.failure(EXERCISE_NOT_FOUND_EXCEPTION);
    }

    /* Notification */
    @ExceptionHandler(FCMTokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response fcmTokenNotFoundExceptionHandler(FCMTokenNotFoundException e){
        log.error(FCM_TOKEN_NOT_FOUND_EXCEPTION.getMessage());
        return Response.failure(FCM_TOKEN_NOT_FOUND_EXCEPTION);
    }
}
