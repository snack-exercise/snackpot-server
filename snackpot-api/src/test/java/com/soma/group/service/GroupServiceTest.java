package com.soma.group.service;

import com.soma.exception.group.AlreadyJoinedGroupException;
import com.soma.group.dto.request.GroupCreateRequest;
import com.soma.group.dto.request.GroupJoinRequest;
import com.soma.group.dto.response.GroupNameResponse;
import com.soma.group.factory.fixtures.GroupFixtures;
import com.soma.group.repository.GroupRepository;
import com.soma.joinList.repository.JoinListRepository;
import com.soma.member.factory.fixtures.MemberFixtures;
import com.soma.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.soma.group.factory.dto.GroupCreateFactory.createGroupCreateRequest;
import static com.soma.group.factory.dto.GroupCreateFactory.createGroupJoinRequest;
import static com.soma.group.factory.entity.GroupFactory.createGroup;
import static com.soma.member.factory.entity.MemberFactory.createUserRoleMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("GroupService 비즈니스 로직 테스트")
class GroupServiceTest {
    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JoinListRepository joinListRepository;
    @DisplayName("그룹 이름을 입력해 그룹을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        GroupCreateRequest request = createGroupCreateRequest();

        //mocking
        given(memberRepository.findByEmailAndStatus(anyString(), any())).willReturn(Optional.of(createUserRoleMember()));

        // when
        GroupNameResponse response = groupService.create(request, MemberFixtures.이메일);

        // then
        assertThat(response.getGroupName()).isEqualTo(request.getGroupName());
    }

    @DisplayName("그룹에 가입한다.")
    @Test
    void join() throws Exception {
        //given
        GroupJoinRequest request = createGroupJoinRequest();

        //mocking
        given(groupRepository.findByCode(anyString())).willReturn(Optional.of(createGroup()));
        given(memberRepository.findByEmailAndStatus(anyString(), any())).willReturn(Optional.of(createUserRoleMember()));
        given(joinListRepository.existsByGroupAndMember(any(), any())).willReturn(false);

        // when
        GroupNameResponse response = groupService.join(request, MemberFixtures.이메일);

        // then
        assertThat(response.getGroupName()).isEqualTo(GroupFixtures.그룹명);
    }

    @DisplayName("기존에 가입된 그룹에 재가입은 불가능하다.")
    @Test
    void canNotJoinAlreadyJoinedGroup() throws Exception {
        //given
        GroupJoinRequest request = createGroupJoinRequest();

        //mocking
        given(groupRepository.findByCode(anyString())).willReturn(Optional.of(createGroup()));
        given(memberRepository.findByEmailAndStatus(anyString(), any())).willReturn(Optional.of(createUserRoleMember()));
        given(joinListRepository.existsByGroupAndMember(any(), any())).willReturn(true);

        // when  // then
        assertThatThrownBy(() -> groupService.join(request, MemberFixtures.이메일)).isInstanceOf(AlreadyJoinedGroupException.class);
    }
}