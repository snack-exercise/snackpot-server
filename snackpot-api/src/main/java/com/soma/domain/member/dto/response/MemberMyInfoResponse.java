package com.soma.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
public class MemberMyInfoResponse {
    private String userName;
    private String profileImg;
    private Integer dailyGoalTime;
    private ArrayList<DailyGoalTime> weeklyGoalTime;

    public static MemberMyInfoResponse toDto(String username, String profileImg, Integer dailyGoalTime, ArrayList<DailyGoalTime> weeklyGoalTime) {
        return new MemberMyInfoResponse(username, profileImg, dailyGoalTime, weeklyGoalTime);
    }
}
