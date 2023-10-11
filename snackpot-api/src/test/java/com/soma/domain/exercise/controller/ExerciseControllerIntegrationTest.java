package com.soma.domain.exercise.controller;

import com.soma.domain.body_part.factory.entity.BodyPartFactory;
import com.soma.domain.bodypart.entity.BodyPart;
import com.soma.domain.bodypart.entity.BodyPartType;
import com.soma.domain.bodypart.repository.BodyPartRepository;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.factory.entity.ExerciseFactory;
import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.domain.exercise_body_part.factory.entity.ExerciseBodyPartFactory;
import com.soma.domain.exercise_bodypart.repository.ExerciseBodypartRepository;
import com.soma.domain.exercise_like.factory.entity.ExerciseLikeFactory;
import com.soma.domain.exercise_like.repository.ExerciseLikeRepository;
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

import java.util.Arrays;

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

    @Autowired private ExerciseLikeRepository exerciseLikeRepository;

    @Autowired
    private BodyPartRepository bodyPartRepository;

    @Autowired
    private ExerciseBodypartRepository exerciseBodypartRepository;

    private Member 회원;

    private BodyPart 상체, 하체, 코어, 전신, 팔, 다리, 등, 가슴, 어깨;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        회원 = memberRepository.save(MemberFactory.createUserRoleMember());

        상체 = BodyPartFactory.createBodyPart(BodyPartType.UPPER_BODY);
        하체 = BodyPartFactory.createBodyPart(BodyPartType.LOWER_BODY);
        코어 = BodyPartFactory.createBodyPart(BodyPartType.CORE);
        전신 = BodyPartFactory.createBodyPart(BodyPartType.FULL_BODY);
        팔 = BodyPartFactory.createBodyPart(BodyPartType.ARMS);
        다리 = BodyPartFactory.createBodyPart(BodyPartType.LEGS);
        등 = BodyPartFactory.createBodyPart(BodyPartType.BACK);
        가슴 = BodyPartFactory.createBodyPart(BodyPartType.CHEST);
        어깨 = BodyPartFactory.createBodyPart(BodyPartType.SHOULDERS);

        bodyPartRepository.saveAll(Arrays.asList(
                상체, 하체, 코어, 전신, 팔, 다리, 등, 가슴, 어깨
        ));
    }

    @Test
    @DisplayName("운동 목록 리스트를 조회한다.")
    @WithMockUser(username = "test@naver.com")
    void readAllByConditionTest() throws Exception {
        //given
        Youtuber 유튜버1 = YoutuberFactory.createYoutuberWithChannelId("1");
        Youtuber 유튜버2 = YoutuberFactory.createYoutuberWithChannelId("2");
        youtuberRepository.save(유튜버1);
        youtuberRepository.save(유튜버2);

        Exercise 운동1 = exerciseRepository.save(ExerciseFactory.createExerciseWithYoutuberAndVideoId(유튜버1, "1"));
        Exercise 운동2 = exerciseRepository.save(ExerciseFactory.createExerciseWithYoutuberAndVideoId(유튜버2, "2"));

        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동1, 상체));
        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동1, 코어));
        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동1, 다리));

        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동2, 하체));
        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동2, 코어));

        clean();

        //when, then
        mockMvc.perform(
                        get("/exercises")
                                .param("size", "5")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andDo(print());
    }

    @Test
    @DisplayName("운동 목록 리스트 운동 부위 필터링을 테스트한다.")
    @WithMockUser(username = "test@naver.com")
    void readAllByConditionAsBodyPartTest() throws Exception {
        //given
        Youtuber 유튜버1 = YoutuberFactory.createYoutuberWithChannelId("1");
        Youtuber 유튜버2 = YoutuberFactory.createYoutuberWithChannelId("2");
        youtuberRepository.save(유튜버1);
        youtuberRepository.save(유튜버2);

        Exercise 운동1 = exerciseRepository.save(ExerciseFactory.createExerciseWithYoutuberAndVideoId(유튜버1, "1"));
        Exercise 운동2 = exerciseRepository.save(ExerciseFactory.createExerciseWithYoutuberAndVideoId(유튜버2, "2"));


        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동1, 상체));
        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동1, 코어));
        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동1, 다리));

        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동2, 하체));
        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동2, 코어));

        clean();

        //when, then
        mockMvc.perform(
                        get("/exercises")
                                .param("size", "5")
                                .param("bodyPartTypes", "LEGS")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andDo(print());
    }

    @Test
    @DisplayName("운동 목록 리스트 좋아요 여부를 테스트한다.")
    @WithMockUser(username = "test@naver.com")
    void readAllByConditionAsIsLikedTest() throws Exception {
        //given
        Youtuber 유튜버1 = YoutuberFactory.createYoutuberWithChannelId("1");
        Youtuber 유튜버2 = YoutuberFactory.createYoutuberWithChannelId("2");
        youtuberRepository.save(유튜버1);
        youtuberRepository.save(유튜버2);

        Exercise 운동1 = exerciseRepository.save(ExerciseFactory.createExerciseWithYoutuberAndVideoId(유튜버1, "1"));
        Exercise 운동2 = exerciseRepository.save(ExerciseFactory.createExerciseWithYoutuberAndVideoId(유튜버2, "2"));


        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동1, 상체));
        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동1, 코어));
        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동1, 다리));

        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동2, 하체));
        exerciseBodypartRepository.save(ExerciseBodyPartFactory.createExerciseBodyPart(운동2, 코어));

        exerciseLikeRepository.save(ExerciseLikeFactory.createExerciseLike(회원, 운동1));
        //exerciseLikeRepository.save(ExerciseLikeFactory.createExerciseLike(회원, 운동2));

        clean();

        //when, then
        mockMvc.perform(
                        get("/exercises")
                                .param("size", "5")
                                .param("like", "true")
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