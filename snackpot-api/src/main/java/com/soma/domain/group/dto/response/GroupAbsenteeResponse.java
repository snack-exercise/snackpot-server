package com.soma.domain.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GroupAbsenteeResponse {
    private String groupCode;
    private List<GroupAbsenteeUser> absentees;

    public static GroupAbsenteeResponse toDto(String groupCode, List<GroupAbsenteeUser> absentees){
        return new GroupAbsenteeResponse(groupCode, absentees);
    }
}
