package com.soma.joinlist.factory;


import com.soma.domain.group.entity.Group;
import com.soma.domain.joinlist.entity.JoinList;
import com.soma.domain.member.entity.Member;

import static com.soma.domain.joinlist.entity.JoinType.HOST;
import static com.soma.domain.joinlist.entity.JoinType.MEMBER;

public class JoinListFactory {
    public static JoinList createHostJoinList(Member member, Group group){
        return  JoinList.builder()
                .member(member)
                .group(group)
                .joinType(HOST)
                .build();
    }

    public static JoinList createMemberJoinList(Member member, Group group){
        return  JoinList.builder()
                .member(member)
                .group(group)
                .joinType(MEMBER)
                .build();
    }
}
