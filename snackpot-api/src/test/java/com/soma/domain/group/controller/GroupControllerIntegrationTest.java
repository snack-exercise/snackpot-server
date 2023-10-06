package com.soma.domain.group.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soma.common.constant.Status;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.factory.entity.ExerciseFactory;
import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.domain.exercise_record.entity.ExerciseRecord;
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
import com.soma.domain.member.factory.fixtures.MemberFixtures;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        Group 그룹 = GroupFactory.createGroup();
        groupRepository.save(그룹);

        Member 회원A = MemberFactory.createUserRoleMemberWithNameAndEmail("회원A", "A@gmail.com");
        Member 회원B = MemberFactory.createUserRoleMemberWithNameAndEmail("회원B", "B@gmail.com");
        Member 회원C = MemberFactory.createUserRoleMemberWithNameAndEmail("회원C", "C@gmail.com");
        Member 회원D = MemberFactory.createUserRoleMemberWithNameAndEmail("회원D", "D@gmail.com");
        memberRepository.saveAll(List.of(회원A, 회원B, 회원C, 회원D));

        joinListRepository.save(JoinListFactory.createHostJoinList(회원A, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원B, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원C, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원D, 그룹));

        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = ExerciseFactory.createExerciseWithYoutuber(유튜버);
        exerciseRepository.save(운동);

        ExerciseRecord 기록1 = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMember(운동, 회원A);
        ExerciseRecord 기록2 = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMember(운동, 회원D);
        exerciseRecordRepository.saveAll(List.of(기록1, 기록2));
        LocalDateTime 하루전날 = LocalDateTime.now().minusDays(1);
        기록2.updateCreatedAt(하루전날);

        //when //then
        mockMvc.perform(
                        get("/groups/{groupId}/absentees", 그룹.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data.groupCode").value(그룹.getCode()))
                .andExpect(jsonPath("$.result.data.absentees", Matchers.containsInAnyOrder(
                        Map.of("name", "회원B", "profileImage", MemberFixtures.프로필사진),
                        Map.of("name", "회원C", "profileImage", MemberFixtures.프로필사진),
                        Map.of("name", "회원D", "profileImage", MemberFixtures.프로필사진))))
                .andDo(print());
    }

    @Test
    @DisplayName("그룹의 일주일동안의 요일별 회원의 운동시간 총합을 조회한다.")
    @WithMockUser(username = "test@naver.com")
    void readExerciseTimeStatics() throws Exception {
        Group 그룹 = GroupFactory.createGroup();
        groupRepository.save(그룹);

        Member 회원A = MemberFactory.createUserRoleMemberWithNameAndEmail("회원A", "A@gmail.com");
        Member 회원B = MemberFactory.createUserRoleMemberWithNameAndEmail("회원B", "B@gmail.com");
        Member 회원C = MemberFactory.createUserRoleMemberWithNameAndEmail("회원C", "C@gmail.com");
        Member 회원D = MemberFactory.createUserRoleMemberWithNameAndEmail("회원D", "D@gmail.com");
        memberRepository.saveAll(List.of(회원A, 회원B, 회원C, 회원D));

        joinListRepository.save(JoinListFactory.createHostJoinList(회원A, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원B, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원C, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원D, 그룹));

        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = ExerciseFactory.createExerciseWithYoutuber(유튜버);
        exerciseRepository.save(운동);

        LocalDateTime 월 = localDateTimeOfThisWeek(1);
        LocalDateTime 수 = localDateTimeOfThisWeek(3);
        LocalDateTime 일 = localDateTimeOfThisWeek(7);

        ExerciseRecord 기록A = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMemberAndTime(운동, 회원A, 10);
        ExerciseRecord 기록B = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMemberAndTime(운동, 회원B, 20);
        ExerciseRecord 기록D = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMemberAndTime(운동, 회원D, 30);
        exerciseRecordRepository.saveAll(List.of(기록A, 기록B, 기록D));
        기록A.updateCreatedAt(월);
        기록B.updateCreatedAt(수);
        기록D.updateCreatedAt(일);

        //when //then
        mockMvc.perform(
                        get("/groups/{groupId}/statistics", 그룹.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data[0].day").value("mon"))
                .andExpect(jsonPath("$.result.data[0].date").value(월.toLocalDate().toString()))
                .andExpect(jsonPath("$.result.data[0].statics", hasSize(4)))
                .andExpect(jsonPath("$.result.data[0].statics", Matchers.containsInAnyOrder(
                        Map.of("userId", 회원A.getId().intValue(), "name", "회원A", "time",  10), // todo: 회원A.getId()로 하면 에러 발생함
                        Map.of("userId", 회원B.getId().intValue(), "name", "회원B", "time", 0),
                        Map.of("userId", 회원C.getId().intValue(), "name", "회원C", "time", 0),
                        Map.of("userId", 회원D.getId().intValue(), "name", "회원D", "time", 0))))
                .andExpect(jsonPath("$.result.data[6].day").value("sun"))
                .andExpect(jsonPath("$.result.data[6].date").value(일.toLocalDate().toString()))
                .andExpect(jsonPath("$.result.data[6].statics", Matchers.containsInAnyOrder(
                        Map.of("userId", 회원A.getId().intValue(), "name", "회원A", "time",  0), // todo: 회원A.getId()로 하면 에러 발생함 - 회원A.getId().intValue()는 테스트 통과... 왜지?
                        Map.of("userId", 회원B.getId().intValue(), "name", "회원B", "time", 0),
                        Map.of("userId", 회원C.getId().intValue(), "name", "회원C", "time", 0),
                        Map.of("userId", 회원D.getId().intValue(), "name", "회원D", "time", 30))))
                .andDo(print());
    }

    @Test
    @DisplayName("그룹 내 일주일간 운동 수행 결과 표를 조회한다.")
    @WithMockUser(username = "test@naver.com")
    void readExerciseCheckListTest() throws Exception {
        Group 그룹 = GroupFactory.createGroup();
        groupRepository.save(그룹);

        Member 회원A = MemberFactory.createUserRoleMemberWithNameAndEmailAndDailyGoalTime("회원A", "A@gmail.com", 10);
        Member 회원B = MemberFactory.createUserRoleMemberWithNameAndEmailAndDailyGoalTime("회원B", "B@gmail.com", 20);
        memberRepository.saveAll(List.of(회원A, 회원B));

        joinListRepository.save(JoinListFactory.createHostJoinList(회원A, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원B, 그룹));

        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = ExerciseFactory.createExerciseWithYoutuber(유튜버);
        exerciseRepository.save(운동);

        LocalDateTime 월 = localDateTimeOfThisWeek(1);
        LocalDateTime 수 = localDateTimeOfThisWeek(3);
        LocalDateTime 일 = localDateTimeOfThisWeek(7);

        ExerciseRecord 기록A = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMemberAndTime(운동, 회원A, 9);
        ExerciseRecord 기록B = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMemberAndTime(운동, 회원A, 11);
        ExerciseRecord 기록C = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMemberAndTime(운동, 회원B, 19);
        ExerciseRecord 기록D = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMemberAndTime(운동, 회원B, 21);
        ExerciseRecord 기록E = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMemberAndTime(운동, 회원B, 25);
        exerciseRecordRepository.saveAll(List.of(기록A, 기록B, 기록C, 기록D, 기록E));
        기록A.updateCreatedAt(월);
        기록B.updateCreatedAt(수);
        기록C.updateCreatedAt(월);
        기록D.updateCreatedAt(수);
        기록E.updateCreatedAt(일);

        //when //then
        mockMvc.perform(
                        get("/groups/{groupId}/checklist", 그룹.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data[0].userId").value(회원A.getId()))
                .andExpect(jsonPath("$.result.data[0].successNum").value(1))
                .andExpect(jsonPath("$.result.data[0].checkList", hasSize(7)))
                .andExpect(jsonPath("$.result.data[0].checkList[*]").value(Matchers.contains("PARTIAL", "UNCHECK", "CHECK", "UNCHECK", "UNCHECK", "UNCHECK", "UNCHECK")))
                .andExpect(jsonPath("$.result.data[1].userId").value(회원B.getId()))
                .andExpect(jsonPath("$.result.data[1].successNum").value(2))
                .andExpect(jsonPath("$.result.data[1].checkList", hasSize(7)))
                .andExpect(jsonPath("$.result.data[1].checkList").value(Matchers.contains("PARTIAL", "UNCHECK", "CHECK", "UNCHECK", "UNCHECK", "UNCHECK", "CHECK")));
//                .andDo(print());
    }
    
    @Test
    @DisplayName("그룹을 삭제한다.")
    @WithMockUser(username = "test@naver.com")
    void deleteGroupTest() throws Exception {
        //given
        Group 그룹 = GroupFactory.createGroup();
        groupRepository.save(그룹);

        Member 회원A = MemberFactory.createUserRoleMemberWithNameAndEmail("회원A", "A@gmail.com");
        Member 회원B = MemberFactory.createUserRoleMemberWithNameAndEmail("회원B", "B@gmail.com");
        Member 회원C = MemberFactory.createUserRoleMemberWithNameAndEmail("회원C", "C@gmail.com");
        Member 회원D = MemberFactory.createUserRoleMemberWithNameAndEmail("회원D", "D@gmail.com");
        memberRepository.saveAll(List.of(회원A, 회원B, 회원C, 회원D));

        joinListRepository.save(JoinListFactory.createHostJoinList(회원A, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원B, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원C, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원D, 그룹));

        int beforeJoinListSize = joinListRepository.findAll().size();
        int beforeGroupSize = groupRepository.findAll().size();
        int beforeMemberSize = memberRepository.findAll().size();

        //when //then
        mockMvc.perform(
                        delete("/groups/{groupId}", 그룹.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andDo(print());

        int afterJoinListSize = joinListRepository.findAll().size();
        int afterGroupSize = groupRepository.findAll().size();
        int afterMemberSize = memberRepository.findAll().size();
        assertThat(afterJoinListSize).isEqualTo(0);
        assertThat(afterGroupSize).isEqualTo(0);
        assertThat(afterMemberSize).isEqualTo(5);
    }

    // 요일에 해당하는 숫자를 입력하면, 이번주의 특정 요일 날짜를 가져온다. 월 1, 일 7
    private LocalDateTime localDateTimeOfThisWeek(int dayOfWeekNum){
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);// 오늘 자정
        int dayOfWeek = today.getDayOfWeek().getValue(); // 오늘 요일(숫자), 월(1), 일(7)
        LocalDateTime monday = today.minusDays(dayOfWeek-1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return monday.plusDays(dayOfWeekNum - 1);
    }


}