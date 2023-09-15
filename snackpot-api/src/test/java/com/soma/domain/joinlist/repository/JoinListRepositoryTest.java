package com.soma.domain.joinlist.repository;

import com.soma.config.QueryDSLConfig;
import com.soma.domain.group.entity.Group;
import com.soma.domain.group.factory.entity.GroupFactory;
import com.soma.domain.member.factory.entity.MemberFactory;
import com.soma.domain.group.repository.GroupRepository;
import com.soma.domain.joinlist.entity.JoinList;
import com.soma.domain.joinlist.entity.JoinType;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfig.class) // @DataJpaTest는 JPA관련 빈만 등록, @JpaQueryFactory도 빈으로 등록해야함
@DisplayName("JoinListRepository JPA 동작 테스트")
class JoinListRepositoryTest {
    @Autowired private JoinListRepository joinListRepository;
    @Autowired private GroupRepository groupRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("회원이 그룹을 가입하지 않으면 false를 반환한다.")
    void existsByGroupAndMemberFalse() throws Exception {
        //given
        Group group = GroupFactory.createGroup();
        groupRepository.save(group);
        Member member = MemberFactory.createUserRoleMember();
        memberRepository.save(member);

        //when
        boolean result = joinListRepository.existsByGroupAndMember(group, member);

        //then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("회원이 그룹을 가입했다면 true를 반환한다.")
    void existsByGroupAndMemberTrue() throws Exception {
        //given
        Group group = GroupFactory.createGroup();
        groupRepository.save(group);
        Member member = MemberFactory.createUserRoleMember();
        memberRepository.save(member);
        joinListRepository.save(
                JoinList.builder()
                .joinType(JoinType.HOST)
                .group(group)
                .member(member)
                .build()
        );

        //when
        boolean result = joinListRepository.existsByGroupAndMember(group, member);

        //then
        Assertions.assertThat(result).isTrue();
    }
}