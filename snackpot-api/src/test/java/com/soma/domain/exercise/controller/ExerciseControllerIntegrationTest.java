package com.soma.domain.exercise.controller;

import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.factory.entity.ExerciseFactory;
import com.soma.domain.exercise.repository.ExerciseRepository;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("ExerciseLikeController 통합 테스트")
class ExerciseControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;

    @Autowired private WebApplicationContext context;

    @PersistenceContext
    private EntityManager em;

    @Autowired private MemberRepository memberRepository;
    @Autowired private ExerciseRepository exerciseRepository;
    @Autowired private YoutuberRepository youtuberRepository;

    private Member 회원;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        회원 = memberRepository.save(MemberFactory.createUserRoleMember());
    }

    @Test
    @DisplayName("운동 목록 리스트를 조회한다.")
    @WithMockUser(username = "test@naver.com")
    void readAllByConditionTest() throws Exception {
        //given
        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = exerciseRepository.save(ExerciseFactory.createExerciseWithYoutuber(유튜버));
        exerciseRepository.save(운동);

        clean();

        //when, then
        mockMvc.perform(
                        get("/exercises")
                                .param("size", "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andDo(print());
    }



    void clean() {
        em.flush();
        em.clear();
    }
}