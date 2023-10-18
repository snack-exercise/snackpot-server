package com.soma.domain.member.controller;

import com.soma.common.constant.Status;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.factory.entity.ExerciseFactory;
import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.domain.exercise_record.entity.ExerciseRecord;
import com.soma.domain.exercise_record.factory.entity.ExerciseRecordFactory;
import com.soma.domain.exercise_record.repository.ExerciseRecordRepository;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.factory.entity.MemberFactory;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.domain.youtuber.entity.Youtuber;
import com.soma.domain.youtuber.factory.entity.YoutuberFactory;
import com.soma.domain.youtuber.repository.YoutuberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("MemberController 통합 테스트")
class MemberControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseRecordRepository exerciseRecordRepository;

    @Autowired
    private YoutuberRepository youtuberRepository;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // security 인증 정보를 제공하기 위한 유저 정보 저장
        memberRepository.save(MemberFactory.createUserRoleMember());
    }

    @Test
    @DisplayName("총 사용자 수를 조회한다.")
    void getTotalNumTest() throws Exception {
        // given
        memberRepository.save(MemberFactory.createUserRoleMemberWithNameAndEmail("회원A", "A@gamil.com"));
        memberRepository.save(MemberFactory.createUserRoleMemberWithNameAndEmail("회원B", "B@gamil.com"));

        // when, then
        Long totalNum = memberRepository.countByStatus(Status.ACTIVE);
        mockMvc.perform(
                        get("/members/total-num")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data.totalNum").value(totalNum));
    }

    @Test
    @DisplayName("마이페이지를 조회한다.")
    @WithMockUser(username = "test@naver.com")
    void readMyInfo() throws Exception {
        // given
        Member 회원 = memberRepository.findByEmailAndStatus("test@naver.com", Status.ACTIVE).get();
        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = ExerciseFactory.createExerciseWithYoutuber(유튜버);
        exerciseRepository.save(운동);

        LocalDateTime 월 = localDateTimeOfThisWeek(1);
        LocalDateTime 수 = localDateTimeOfThisWeek(3);
        LocalDateTime 일 = localDateTimeOfThisWeek(7);

        ExerciseRecord 기록A = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMemberAndTime(운동, 회원, 9);
        ExerciseRecord 기록B = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMemberAndTime(운동, 회원, 11);
        ExerciseRecord 기록C = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMemberAndTime(운동, 회원, 19);
        ExerciseRecord 기록D = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMemberAndTime(운동, 회원, 21);
        ExerciseRecord 기록E = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMemberAndTime(운동, 회원, 25);
        exerciseRecordRepository.saveAll(List.of(기록A, 기록B, 기록C, 기록D, 기록E));
        기록A.updateCreatedAt(월);
        기록B.updateCreatedAt(수);
        기록C.updateCreatedAt(월);
        기록D.updateCreatedAt(수);
        기록E.updateCreatedAt(일);

        clean();

        //when, then
        mockMvc.perform(
                        get("/members/my")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data.userName").value(회원.getName()))
                .andExpect(jsonPath("$.result.data.weeklyGoalTime[0].time").value(기록A.getTime() + 기록C.getTime()))
                .andExpect(jsonPath("$.result.data.weeklyGoalTime[2].time").value(기록B.getTime() + 기록D.getTime()))
                .andExpect(jsonPath("$.result.data.weeklyGoalTime[6].time").value(기록E.getTime()));



    }

    private LocalDateTime localDateTimeOfThisWeek(int dayOfWeekNum){
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);// 오늘 자정
        int dayOfWeek = today.getDayOfWeek().getValue(); // 오늘 요일(숫자), 월(1), 일(7)
        LocalDateTime monday = today.minusDays(dayOfWeek-1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return monday.plusDays(dayOfWeekNum - 1);
    }

    private void clean() {
        em.flush();
        em.clear();
    }
}