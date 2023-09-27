package com.soma.domain.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.factory.entity.ExerciseFactory;
import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.domain.member.factory.entity.MemberFactory;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.domain.rating.entity.RatingType;
import com.soma.domain.rating.repository.RatingRepository;
import com.soma.domain.review.dto.request.ReviewCreateRequest;
import com.soma.domain.review.factory.dto.ReviewCreateFactory;
import com.soma.domain.review.factory.fixtures.ReivewFixtures;
import com.soma.domain.review.repository.ReviewRepository;
import com.soma.domain.youtuber.entity.Youtuber;
import com.soma.domain.youtuber.factory.entity.YoutuberFactory;
import com.soma.domain.youtuber.repository.YoutuberRepository;
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
@DisplayName("ReviewController 통합 테스트")
class ReviewControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private WebApplicationContext context;

    @Autowired private MemberRepository memberRepository;
    @Autowired private RatingRepository ratingRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ExerciseRepository exerciseRepository;
    @Autowired private YoutuberRepository youtuberRepository;

    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        memberRepository.save(MemberFactory.createUserRoleMember());
    }


    @Test
    @DisplayName("리뷰와 평가를 모두 작성한다.")
    @WithMockUser(username = "test@naver.com")
    void createTest() throws Exception {
        //given
        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = ExerciseFactory.createExerciseWithYoutuber(유튜버);
        exerciseRepository.save(운동);

        ReviewCreateRequest request = ReviewCreateFactory.createReviewCreateRequest(운동.getId(), RatingType.GOOD, ReivewFixtures.리뷰내용);
        int beforeReviewSize = reviewRepository.findAll().size();
        int beforeRatingSize = ratingRepository.findAll().size();

        //when //then
        mockMvc.perform(
                        post("/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andDo(print());

        int afterReviewSize = reviewRepository.findAll().size();
        int afterRatingSize = ratingRepository.findAll().size();
        assertThat(afterReviewSize).isEqualTo(beforeReviewSize + 1);
        assertThat(afterRatingSize).isEqualTo(beforeRatingSize + 1);
    }

}