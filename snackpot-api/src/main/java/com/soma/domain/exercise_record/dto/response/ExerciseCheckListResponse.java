package com.soma.domain.exercise_record.dto.response;

import com.soma.domain.exercise_record.dto.CheckList;
import com.soma.domain.group.dto.response.UserTimeStaticsResponse;
import com.soma.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.soma.domain.exercise_record.dto.CheckList.*;

@Builder
@AllArgsConstructor
@Getter
public class ExerciseCheckListResponse {
    private String userName;
    private Long userId;
    private List<CheckList> checkList;
    private Integer successNum; // 일주일 중 성공한 횟수
    public static ExerciseCheckListResponse toDto(Member member){
        return ExerciseCheckListResponse.builder()
                .userId(member.getId())
                .userName(member.getName())
                .checkList(new ArrayList<>(Collections.nCopies(7, UNCHECK)))
                .successNum(7)
                .build();
    }

    public void updateCheckList(Member member, LocalDate localDate, UserTimeStaticsResponse userTimeStaticsResponse) {
        int idx = localDate.getDayOfWeek().getValue()-1;
        if(userTimeStaticsResponse.getTime() == 0) {
            checkList.set(idx, UNCHECK);
        }else if(member.getDailyGoalTime() > userTimeStaticsResponse.getTime()){
            checkList.set(idx, PARTIAL);
        }else {
            checkList.set(idx, CHECK);
        }
    }

    public void updateSuccessNum(){
        successNum = Collections.frequency(checkList, CHECK);
    }
}
