package com.soma.domain.group.dto.response;

import com.soma.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupAbsenteeResponse {
    private String name;
    private String profileImage;

    public static GroupAbsenteeResponse toDto(Member member){
        return new GroupAbsenteeResponse(member.getName(), member.getProfileImg());
    }
}
