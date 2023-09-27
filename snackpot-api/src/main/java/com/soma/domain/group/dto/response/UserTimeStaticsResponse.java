package com.soma.domain.group.dto.response;

import com.soma.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserTimeStaticsResponse {
    private Long userId;
    private String name;
    private int time;

    public static UserTimeStaticsResponse toDto(Member member, int time){
        return new UserTimeStaticsResponse(member.getId(), member.getName(), time);
    }

    public void add(int time){
        this.time += time;
    }
}
