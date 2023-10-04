package com.soma.domain.group.dto.response;

import com.soma.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupAbsenteeUser {
    private String name;
    private String profileImage;

    public static GroupAbsenteeUser toDto(Member member){
        return new GroupAbsenteeUser(member.getName(), member.getProfileImg());
    }
}
