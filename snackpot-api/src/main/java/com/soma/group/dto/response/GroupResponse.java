package com.soma.group.dto.response;

import com.soma.group.entity.Group;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class GroupResponse {
    private Long id;

    private String name;

    private LocalDate startDate; // 시작 기간

    private String code; // 그룹 입장 코드


    public static GroupResponse toDto(Group group){
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .startDate(group.getStartDate())
                .code(group.getCode())
                .build();
    }

}
