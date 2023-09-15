package com.soma.group.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soma.group.dto.request.GroupCreateRequest;
import com.soma.group.dto.request.GroupJoinRequest;
import com.soma.group.dto.response.GroupNameResponse;
import com.soma.group.factory.dto.GroupCreateFactory;
import com.soma.group.factory.fixtures.GroupFixtures;
import com.soma.group.service.GroupService;
import com.soma.member.factory.fixtures.MemberFixtures;
import com.soma.security.TestUserArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("GroupController 단위 테스트")
class GroupControllerTest {
    @InjectMocks
    private GroupController groupController;
    @Mock
    GroupService groupService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController)
                .setCustomArgumentResolvers(new TestUserArgumentResolver())
//                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("그룹 이름을 입력해 그룹을 생성한다.")
    void create() throws Exception {
        //given
        GroupCreateRequest request = GroupCreateFactory.createGroupCreateRequest();
        GroupNameResponse response = new GroupNameResponse(request.getGroupName());
//        when(groupService.create(request, MemberFixtures.이메일)).thenReturn(response); // todo 왜 이건 안되는거지....?
        when(groupService.create(any(), eq(MemberFixtures.이메일))).thenReturn(response);

        //when //then
        mockMvc.perform(
                post("/groups")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data.groupName").value(request.getGroupName()))
                .andDo(print());
    }

    @Test
    @DisplayName("그룹에 가입한다.")
    void join() throws Exception {
        //given
        GroupJoinRequest request = GroupCreateFactory.createGroupJoinRequest();
        GroupNameResponse response = new GroupNameResponse(GroupFixtures.그룹명);
//        when(groupService.create(request, MemberFixtures.이메일)).thenReturn(response); // todo 왜 이건 안되는거지....?
        when(groupService.join(any(), anyString())).thenReturn(response);

        //when //then
        mockMvc.perform(
                        post("/groups/join")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data.groupName").value(GroupFixtures.그룹명))
                .andDo(print());
    }


}