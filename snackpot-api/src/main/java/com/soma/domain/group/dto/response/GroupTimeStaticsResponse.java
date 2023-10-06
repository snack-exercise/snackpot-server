package com.soma.domain.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class GroupTimeStaticsResponse {
    private String day;
    private LocalDate date;
    private List<UserTimeStaticsResponse> statics;

    public static GroupTimeStaticsResponse toDto(LocalDate date, List<UserTimeStaticsResponse> userTimeStaticsResponseList) {
        return new GroupTimeStaticsResponse(date.getDayOfWeek().name().toLowerCase().substring(0, 3),
                date,
                userTimeStaticsResponseList);
    }

}
