package com.soma.domain.joinlist.entity;

import com.soma.common.BaseEntity;

import com.soma.domain.group.entity.Group;
import com.soma.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import static com.soma.domain.joinlist.entity.JoinType.HOST;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class JoinList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId")
    private Group group;

    @Enumerated(EnumType.STRING)
    private JoinType joinType;

    @Builder
    public JoinList(Member member, Group group, JoinType joinType) {
        this.member = member;
        this.group = group;
        this.joinType = joinType;
        active();
    }

    public static JoinList createHostJoinList(Member member, Group group){
        return JoinList.builder()
                .member(member)
                .group(group)
                .joinType(HOST)
                .build();
    }

    public static JoinList createMemberJoinList(Member member, Group group){
        return JoinList.builder()
                .member(member)
                .group(group)
                .joinType(HOST)
                .build();
    }

}
