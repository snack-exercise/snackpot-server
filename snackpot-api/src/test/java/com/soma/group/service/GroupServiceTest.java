package com.soma.group.service;

import com.soma.group.dto.request.GroupCreateRequest;
import com.soma.group.dto.response.GroupCreateResponse;
import com.soma.group.repository.GroupRepository;
import com.soma.joinlist.repository.JoinListRepository;
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
import static com.soma.member.factory.entity.MemberFactory.createUserRoleMember;
import static org.assertj.core.api.Assertions.assertThat;
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
        GroupCreateResponse response = groupService.create(request, MemberFixtures.이메일);

        // then
        assertThat(response.getGroupName()).isEqualTo(request.getGroupName());
    }
}