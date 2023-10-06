package com.soma.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DailyGoalTime {
    private String day;
    private Integer time;

    public static DailyGoalTime toDto(String day, Integer time) {
        return new DailyGoalTime(day, time);
    }
}
