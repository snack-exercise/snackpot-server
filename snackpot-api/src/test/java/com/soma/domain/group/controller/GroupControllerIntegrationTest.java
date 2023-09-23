package com.soma.domain.group.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soma.domain.group.dto.request.GroupCreateRequest;
import com.soma.domain.group.dto.request.GroupJoinRequest;
import com.soma.domain.group.factory.dto.GroupCreateFactory;
import com.soma.domain.group.factory.entity.GroupFactory;
import com.soma.domain.group.factory.fixtures.GroupFixtures;
import com.soma.domain.member.factory.entity.MemberFactory;
import com.soma.domain.group.repository.GroupRepository;
import com.soma.domain.joinlist.repository.JoinListRepository;
import com.soma.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("GroupController 통합 테스트")
public class GroupControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JoinListRepository joinListRepository;
//    @Autowired
//    private TestInitDB initDB;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
//                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .build();

        // security 인증 정보를 제공하기 위한 유저 정보 저장
        memberRepository.save(MemberFactory.createUserRoleMember());
    }


    @Test
    @DisplayName("그룹 이름을 입력해 그룹을 생성한다.")
    @WithMockUser(username = "test@naver.com")
    void createTest() throws Exception {
        //given
        GroupCreateRequest request = GroupCreateFactory.createGroupCreateRequest();
        int beforeSize = groupRepository.findAll().size();

        //when //then
        mockMvc.perform(
                post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data.groupName").value(request.getGroupName()))
                .andDo(print());

        int afterSize = groupRepository.findAll().size();
        assertThat(afterSize).isEqualTo(beforeSize + 1);
    }

    @Test
    @DisplayName("그룹을 가입한다.")
    @WithMockUser(username = "test@naver.com")
    void joinTest() throws Exception {
        //given
        GroupJoinRequest request = GroupCreateFactory.createGroupJoinRequest();
        groupRepository.save(GroupFactory.createGroup());
        int beforeSize = joinListRepository.findAll().size();

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

        int afterSize = joinListRepository.findAll().size();
        assertThat(afterSize).isEqualTo(beforeSize + 1);
    }
}