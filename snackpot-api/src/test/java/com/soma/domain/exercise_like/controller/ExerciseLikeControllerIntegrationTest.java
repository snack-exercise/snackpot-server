package com.soma.domain.exercise_like.controller;

import com.soma.common.constant.Status;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.factory.entity.ExerciseFactory;
import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.domain.exercise_like.entity.ExerciseLike;
import com.soma.domain.exercise_like.factory.entity.ExerciseLikeFactory;
import com.soma.domain.exercise_like.repository.ExerciseLikeRepository;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.factory.entity.MemberFactory;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.domain.youtuber.entity.Youtuber;
import com.soma.domain.youtuber.factory.entity.YoutuberFactory;
import com.soma.domain.youtuber.repository.YoutuberRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("ExerciseLikeController 통합 테스트")
class ExerciseLikeControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired private WebApplicationContext context;
    @Autowired private ExerciseLikeRepository exerciseLikeRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ExerciseRepository exerciseRepository;
    @Autowired private YoutuberRepository youtuberRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        memberRepository.save(MemberFactory.createUserRoleMember());
    }

    @Test
    @DisplayName("운동 좋아요를 생성한다.")
    @WithMockUser(username = "test@naver.com")
    void createTest() throws Exception {
        //given
        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = exerciseRepository.save(ExerciseFactory.createExerciseWithYoutuber(유튜버));
        exerciseRepository.save(운동);

        Integer beforeSize = exerciseLikeRepository.findAll().size();

        //when, then
        mockMvc.perform(
                        post("/exercises/{exerciseId}/likes", 운동.getId())
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"));

        int afterSize = exerciseLikeRepository.findAll().size();
        assertThat(afterSize).isEqualTo(beforeSize + 1);
    }

    @Test
    @DisplayName("운동 좋아요를 취소한다.")
    @WithMockUser(username = "test@naver.com")
    void deleteTest() throws Exception {
        //given
        Member 사용자 = memberRepository.findByEmailAndStatus("test@naver.com", Status.ACTIVE).get();

        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = exerciseRepository.save(ExerciseFactory.createExerciseWithYoutuber(유튜버));
        exerciseRepository.save(운동);

        ExerciseLike 운동좋아요 = ExerciseLikeFactory.createExerciseLike(사용자, 운동);
        exerciseLikeRepository.save(운동좋아요);

        Integer beforeSize = exerciseLikeRepository.findAll().size();

        //when, then
        mockMvc.perform(
                        delete("/exercises/{exerciseId}/likes", 운동.getId())
                ).andExpect(status().isNoContent())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"));

        int afterSize = exerciseLikeRepository.findAll().size();
        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }
}