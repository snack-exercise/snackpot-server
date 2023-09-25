package com.soma.domain.group.repository;

import com.soma.config.QueryDSLConfig;

import com.soma.domain.group.dto.response.GroupListResponse;
import com.soma.domain.group.entity.Group;
import com.soma.domain.group.factory.entity.GroupFactory;
import com.soma.domain.joinlist.repository.JoinListRepository;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.joinlist.factory.JoinListFactory;

import com.soma.member.factory.entity.MemberFactory;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfig.class) // @DataJpaTest는 JPA관련 빈만 등록, @JpaQueryFactory도 빈으로 등록해야함
@DisplayName("GroupRepositoryCustomImpl JPA 동작 테스트")
class GroupRepositoryCustomImplTest {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JoinListRepository joinListRepository;

    @Test
    @DisplayName("동일한 날짜가 여러개인 상황에서 커서기반 페이지네이션은 그룹 ID를 사용해 커서 다음의 데이터를 조회한다.")
    void groupsWithSameStartDateTest() throws Exception {
        //given
        Member 사용자A = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자A", "A@gmail.com");
        Member 사용자B = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자B", "B@gmail.com");
        Member 사용자C = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자C", "C@gmail.com");
        Member 사용자D = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자D", "D@gmail.com");
        memberRepository.saveAll(List.of(사용자A, 사용자B, 사용자C, 사용자D));

        Group 그룹A = GroupFactory.createGroupWithNameAndStartDate( "그룹A", LocalDate.of(2023, 9, 1));
        Group 그룹B = GroupFactory.createGroupWithNameAndStartDate("그룹B", LocalDate.of(2023,9,3));
        Group 그룹C = GroupFactory.createGroupWithNameAndStartDate("그룹C", LocalDate.of(2023,9,3));
        Group 그룹D = GroupFactory.createGroupWithNameAndStartDate("그룹D", LocalDate.of(2023,9,3));
        Group 그룹E = GroupFactory.createGroupWithNameAndStartDate("그룹E", LocalDate.of(2023,9,3));
        groupRepository.saveAll(List.of(그룹A, 그룹B, 그룹C, 그룹D, 그룹E));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자A, 그룹A));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자B, 그룹B));
        joinListRepository.save(JoinListFactory.createMemberJoinList(사용자A, 그룹B));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자C, 그룹C));
        joinListRepository.save(JoinListFactory.createMemberJoinList(사용자A, 그룹C));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자D, 그룹D));
        joinListRepository.save(JoinListFactory.createMemberJoinList(사용자A, 그룹D));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자A, 그룹E));


        //when
        Slice<GroupListResponse> allByCursor = groupRepository.findAllByCursor(사용자A, 그룹D.getId(), PageRequest.of(0, 2));

        //then
        assertThat(allByCursor.getContent()).hasSize(2)
                .extracting("groupId", "groupName", "startDate", "hostName", "groupNumber")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(그룹C.getId(), 그룹C.getName(), LocalDate.of(2023,9,3), "사용자C", 2),
                        Tuple.tuple(그룹B.getId(), 그룹B.getName(), LocalDate.of(2023,9,3), "사용자B", 2)
                );
        assertThat(allByCursor.hasNext()).isTrue();
    }

    @Test
    @DisplayName("다음 데이터가 충분하지 않은 경우, 남은 데이터만 준다.")
    void groupsWithLastGroupTest() throws Exception {
        //given
        Member 사용자A = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자A", "A@gmail.com");
        Member 사용자B = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자B", "B@gmail.com");
        Member 사용자C = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자C", "C@gmail.com");
        Member 사용자D = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자D", "D@gmail.com");
        memberRepository.saveAll(List.of(사용자A, 사용자B, 사용자C, 사용자D));

        Group 그룹A = GroupFactory.createGroupWithNameAndStartDate( "그룹A", LocalDate.of(2023, 9, 1));
        Group 그룹B = GroupFactory.createGroupWithNameAndStartDate("그룹B", LocalDate.of(2023,9,3));
        Group 그룹C = GroupFactory.createGroupWithNameAndStartDate("그룹C", LocalDate.of(2023,9,3));
        Group 그룹D = GroupFactory.createGroupWithNameAndStartDate("그룹D", LocalDate.of(2023,9,3));
        Group 그룹E = GroupFactory.createGroupWithNameAndStartDate("그룹E", LocalDate.of(2023,9,3));
        groupRepository.saveAll(List.of(그룹A, 그룹B, 그룹C, 그룹D, 그룹E));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자A, 그룹A));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자B, 그룹B));
        joinListRepository.save(JoinListFactory.createMemberJoinList(사용자A, 그룹B));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자C, 그룹C));
        joinListRepository.save(JoinListFactory.createMemberJoinList(사용자A, 그룹C));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자D, 그룹D));
        joinListRepository.save(JoinListFactory.createMemberJoinList(사용자A, 그룹D));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자A, 그룹E));


        //when
        Slice<GroupListResponse> allByCursor = groupRepository.findAllByCursor(사용자A, 그룹B.getId(), PageRequest.of(0, 2));

        //then
        assertThat(allByCursor.getContent()).hasSize(1)
                .extracting("groupId", "groupName", "startDate", "hostName", "groupNumber")
                .contains(
                        Tuple.tuple(그룹A.getId(), 그룹A.getName(), LocalDate.of(2023,9,1), "사용자A", 1)
                );
        assertThat(allByCursor.hasNext()).isFalse();
    }

    @Test
    @DisplayName("처음 그룹 리스트를 조회할 때는, 커서 기반 페이징이 아닌 오프셋 기반 페이징을 사용")
    void findFirstGroupListTest() throws Exception {
        //given
        Member 사용자A = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자A", "A@gmail.com");
        Member 사용자B = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자B", "B@gmail.com");
        Member 사용자C = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자C", "C@gmail.com");
        Member 사용자D = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자D", "D@gmail.com");
        memberRepository.saveAll(List.of(사용자A, 사용자B, 사용자C, 사용자D));

        Group 그룹A = GroupFactory.createGroupWithNameAndStartDate( "그룹A", LocalDate.of(2023, 9, 1));
        Group 그룹B = GroupFactory.createGroupWithNameAndStartDate("그룹B", LocalDate.of(2023,9,3));
        Group 그룹C = GroupFactory.createGroupWithNameAndStartDate("그룹C", LocalDate.of(2023,9,3));
        Group 그룹D = GroupFactory.createGroupWithNameAndStartDate("그룹D", LocalDate.of(2023,9,3));
        Group 그룹E = GroupFactory.createGroupWithNameAndStartDate("그룹E", LocalDate.of(2023,9,3));
        groupRepository.saveAll(List.of(그룹A, 그룹B, 그룹C, 그룹D, 그룹E));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자A, 그룹A));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자B, 그룹B));
        joinListRepository.save(JoinListFactory.createMemberJoinList(사용자A, 그룹B));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자C, 그룹C));
        joinListRepository.save(JoinListFactory.createMemberJoinList(사용자A, 그룹C));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자D, 그룹D));
        joinListRepository.save(JoinListFactory.createMemberJoinList(사용자A, 그룹D));

        joinListRepository.save(JoinListFactory.createHostJoinList(사용자A, 그룹E));


        //when
        Slice<GroupListResponse> firstGroupList = groupRepository.findFirstGroupList(사용자A, PageRequest.of(0, 2));

        //then
        assertThat(firstGroupList.getContent()).hasSize(2)
                .extracting("groupId", "groupName", "startDate", "hostName", "groupNumber")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(그룹E.getId(), 그룹E.getName(), 그룹E.getStartDate(), "사용자A", 1),
                        Tuple.tuple(그룹D.getId(), 그룹D.getName(), 그룹D.getStartDate(), "사용자D", 2)
                );
        assertThat(firstGroupList.hasNext()).isTrue();
    }
}