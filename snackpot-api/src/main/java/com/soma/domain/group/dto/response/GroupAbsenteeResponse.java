package com.soma.domain.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GroupAbsenteeResponse {
    private List<String> usernames;
    public static GroupAbsenteeResponse toDto(List<String> absentees){
        return new GroupAbsenteeResponse(absentees);
    }
}
