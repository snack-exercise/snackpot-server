package com.soma.group.dto.response;

import com.soma.group.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupNameResponse {// todo: 인텔리제이가 record 타입으로 변경 추천
    private final String groupName;
    public static GroupNameResponse toDto(Group group){
        return new GroupNameResponse(group.getName());
    }

}
