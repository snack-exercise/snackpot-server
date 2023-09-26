package com.soma.domain.group.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soma.common.constant.Status;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.factory.entity.ExerciseFactory;
import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.domain.exercise_record.factory.entity.ExerciseRecordFactory;
import com.soma.domain.exercise_record.repository.ExerciseRecordRepository;
import com.soma.domain.group.dto.request.GroupCreateRequest;
import com.soma.domain.group.dto.request.GroupJoinRequest;
import com.soma.domain.group.entity.Group;
import com.soma.domain.group.factory.dto.GroupCreateFactory;
import com.soma.domain.group.factory.entity.GroupFactory;
import com.soma.domain.group.factory.fixtures.GroupFixtures;
import com.soma.domain.group.repository.GroupRepository;
import com.soma.domain.joinlist.repository.JoinListRepository;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.factory.entity.MemberFactory;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.domain.youtuber.entity.Youtuber;
import com.soma.domain.youtuber.factory.entity.YoutuberFactory;
import com.soma.domain.youtuber.repository.YoutuberRepository;
import com.soma.joinlist.factory.JoinListFactory;
import org.hamcrest.Matchers;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired private WebApplicationContext context;
    @Autowired private GroupRepository groupRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private JoinListRepository joinListRepository;
    @Autowired private ExerciseRepository exerciseRepository;
    @Autowired private ExerciseRecordRepository exerciseRecordRepository;
    @Autowired private YoutuberRepository youtuberRepository;
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

    @Test
    @DisplayName("가입한 그룹 목록을 커서를 사용해 조회한다.")
    @WithMockUser(username = "test@naver.com")
    void readGroupListTest() throws Exception {
        //given
        Member 사용자A = memberRepository.findByEmailAndStatus("test@naver.com", Status.ACTIVE).get();
        Member 사용자B = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자B", "B@gmail.com");
        Member 사용자C = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자C", "C@gmail.com");
        Member 사용자D = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자D", "D@gmail.com");
        memberRepository.saveAll(List.of(사용자B, 사용자C, 사용자D));

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

        //when //then
        mockMvc.perform(
                        get("/groups")
                                .param("groupIdCursor", 그룹C.getId().toString())
                                .param("startDateCursor", LocalDate.of(2023,9,3).toString())
                                .param("size", "3")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data.content[0].groupId").value(그룹B.getId()))
                .andDo(print());
    }

    @Test
    @DisplayName("가입한 그룹 목록을 처음 조회한다.")
    @WithMockUser(username = "test@naver.com")
    void readFirst3GroupListTest() throws Exception {
        //given
        Member 사용자A = memberRepository.findByEmailAndStatus("test@naver.com", Status.ACTIVE).get();
        Member 사용자B = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자B", "B@gmail.com");
        Member 사용자C = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자C", "C@gmail.com");
        Member 사용자D = MemberFactory.createUserRoleMemberWithNameAndEmail("사용자D", "D@gmail.com");
        memberRepository.saveAll(List.of(사용자B, 사용자C, 사용자D));

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

        //when //then
        mockMvc.perform(
                        get("/groups")
                                .param("size", "3")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data.content[0].groupId").value(그룹E.getId()))
                .andDo(print());
    }

    @Test
    @DisplayName("그룹에서 오늘 운동을 수행하지 않은 회원 목록을 조회한다.")
    @WithMockUser(username = "test@naver.com")
    void readAllAbsentees() throws Exception {
        //given
        Group 그룹 = GroupFactory.createGroup();
        groupRepository.save(그룹);

        Member 회원A = MemberFactory.createUserRoleMemberWithNameAndEmail("회원A", "A@gmail.com");
        Member 회원B = MemberFactory.createUserRoleMemberWithNameAndEmail("회원B", "B@gmail.com");
        Member 회원C = MemberFactory.createUserRoleMemberWithNameAndEmail("회원C", "C@gmail.com");
        memberRepository.saveAll(List.of(회원A, 회원B, 회원C));

        joinListRepository.save(JoinListFactory.createHostJoinList(회원A, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원B, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원C, 그룹));

        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = ExerciseFactory.createExerciseWithYoutuber(유튜버);
        exerciseRepository.save(운동);

        exerciseRecordRepository.save(ExerciseRecordFactory.createExerciseRecordWithExerciseAndMember(운동, 회원A));

        //when //then
        mockMvc.perform(
                        get("/groups/{groupId}/absentees", 그룹.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data.usernames", Matchers.containsInAnyOrder("회원B", "회원C")))
                .andDo(print());
    }

}