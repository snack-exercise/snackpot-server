package com.soma.domain.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.factory.entity.ExerciseFactory;
import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.factory.entity.MemberFactory;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.domain.rating.entity.RatingType;
import com.soma.domain.rating.repository.RatingRepository;
import com.soma.domain.review.dto.request.ReviewCreateRequest;
import com.soma.domain.review.entity.Review;
import com.soma.domain.review.factory.ReviewFactory;
import com.soma.domain.review.factory.dto.ReviewCreateFactory;
import com.soma.domain.review.factory.fixtures.ReviewFixtures;
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

import java.time.LocalDateTime;
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
@DisplayName("ReviewController 통합 테스트")
class ReviewControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private YoutuberRepository youtuberRepository;

    @Autowired
    private ObjectMapper objectMapper;

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

        ReviewCreateRequest request = ReviewCreateFactory.createReviewCreateRequest(운동.getId(), RatingType.GOOD, ReviewFixtures.리뷰내용);
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

    @Test
    @DisplayName("공백이나 빈 문자열의 리뷰는 작성할 수 없다.")
    @WithMockUser(username = "test@naver.com")
    void createBlankReviewTest() throws Exception {
        //given
        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = ExerciseFactory.createExerciseWithYoutuber(유튜버);
        exerciseRepository.save(운동);

        ReviewCreateRequest request = ReviewCreateFactory.createReviewCreateRequest(운동.getId(), RatingType.GOOD, "   ");
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
        assertThat(afterReviewSize).isEqualTo(beforeReviewSize);
        assertThat(afterRatingSize).isEqualTo(beforeRatingSize + 1);
    }

    @Test
    @DisplayName("첫번째 리뷰 목록을 받아온다.")
    @WithMockUser(username = "test@naver.com")
    void readFirstReviewListTest() throws Exception {
        //given
        Member 회원A = MemberFactory.createUserRoleMemberWithNameAndEmail("회원A", "A@gmail.com");
        memberRepository.save(회원A);

        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = ExerciseFactory.createExerciseWithYoutuber(유튜버);
        exerciseRepository.save(운동);

        Review 리뷰1 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰1");
        Review 리뷰2 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰2");
        Review 리뷰3 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰3");
        Review 리뷰4 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰4");
        Review 리뷰5 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰5");
        reviewRepository.saveAll(List.of(리뷰1, 리뷰2, 리뷰3, 리뷰4, 리뷰5));
        리뷰1.updateCreatedAt(LocalDateTime.of(2023, 10, 5, 2, 0, 0));
        리뷰2.updateCreatedAt(LocalDateTime.of(2023, 10, 2, 0, 0, 0));
        리뷰3.updateCreatedAt(LocalDateTime.of(2023, 10, 3, 0, 0, 0));
        리뷰4.updateCreatedAt(LocalDateTime.of(2023, 10, 4, 0, 0, 0));
        리뷰5.updateCreatedAt(LocalDateTime.of(2023, 10, 5, 1, 0, 0));

        //when //then
        mockMvc.perform(
                        get("/reviews/exercises/{exerciseId}", 운동.getId().toString())
                                .param("size", "3")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data.totalReviewNum").value("3"))
                .andExpect(jsonPath("$.result.data.reviews.last").value(false))
                .andExpect(jsonPath("$.result.data.reviews.content[0].reviewId").value(리뷰1.getId()))
                .andExpect(jsonPath("$.result.data.reviews.content[1].reviewId").value(리뷰5.getId()))
                .andExpect(jsonPath("$.result.data.reviews.content[2].reviewId").value(리뷰4.getId()));
    }

    @Test
    @DisplayName("리뷰 목록을 받아올 때, size 쿼리 파라미터 값을 입력하지 않아도 default로 5가 설정된다.")
    @WithMockUser(username = "test@naver.com")
    void readFirstReviewListWithDefaultSizeTest() throws Exception {
        //given
        Member 회원A = MemberFactory.createUserRoleMemberWithNameAndEmail("회원A", "A@gmail.com");
        memberRepository.save(회원A);

        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = ExerciseFactory.createExerciseWithYoutuber(유튜버);
        exerciseRepository.save(운동);

        Review 리뷰1 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰1");
        Review 리뷰2 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰2");
        Review 리뷰3 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰3");
        Review 리뷰4 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰4");
        Review 리뷰5 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰5");
        reviewRepository.saveAll(List.of(리뷰1, 리뷰2, 리뷰3, 리뷰4, 리뷰5));
        리뷰1.updateCreatedAt(LocalDateTime.of(2023, 10, 5, 2, 0, 0));
        리뷰2.updateCreatedAt(LocalDateTime.of(2023, 10, 2, 0, 0, 0));
        리뷰3.updateCreatedAt(LocalDateTime.of(2023, 10, 3, 0, 0, 0));
        리뷰4.updateCreatedAt(LocalDateTime.of(2023, 10, 4, 0, 0, 0));
        리뷰5.updateCreatedAt(LocalDateTime.of(2023, 10, 5, 1, 0, 0));

        //when //then
        mockMvc.perform(
                        get("/reviews/exercises/{exerciseId}", 운동.getId().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data.totalReviewNum").value("5"))
                .andExpect(jsonPath("$.result.data.reviews.last").value(true))
                .andExpect(jsonPath("$.result.data.reviews.content[0].reviewId").value(리뷰1.getId()))
                .andExpect(jsonPath("$.result.data.reviews.content[1].reviewId").value(리뷰5.getId()))
                .andExpect(jsonPath("$.result.data.reviews.content[2].reviewId").value(리뷰4.getId()))
                .andExpect(jsonPath("$.result.data.reviews.content[3].reviewId").value(리뷰3.getId()))
                .andExpect(jsonPath("$.result.data.reviews.content[4].reviewId").value(리뷰2.getId()));
    }
    @Test
    @DisplayName("두번째 이상의 리뷰 목록을 받아온다.")
    @WithMockUser(username = "test@naver.com")
    void readReviewListTest() throws Exception {
        //given
        Member 회원A = MemberFactory.createUserRoleMemberWithNameAndEmail("회원A", "A@gmail.com");
        memberRepository.save(회원A);

        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = ExerciseFactory.createExerciseWithYoutuber(유튜버);
        exerciseRepository.save(운동);

        Review 리뷰1 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰1");
        Review 리뷰2 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰2");
        Review 리뷰3 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰3");
        Review 리뷰4 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰4");
        Review 리뷰5 = ReviewFactory.createReviewWithMemberAndExerciseAndContent(회원A, 운동, "리뷰5");
        reviewRepository.saveAll(List.of(리뷰1, 리뷰2, 리뷰3, 리뷰4, 리뷰5));
        리뷰1.updateCreatedAt(LocalDateTime.of(2023, 10, 5, 2, 0, 0));
        리뷰2.updateCreatedAt(LocalDateTime.of(2023, 10, 2, 0, 0, 0));
        리뷰3.updateCreatedAt(LocalDateTime.of(2023, 10, 3, 0, 0, 0));
        리뷰4.updateCreatedAt(LocalDateTime.of(2023, 10, 4, 0, 0, 0));
        리뷰5.updateCreatedAt(LocalDateTime.of(2023, 10, 5, 1, 0, 0));

        //when //then
        mockMvc.perform(
                        get("/reviews/exercises/{exerciseId}", 운동.getId().toString())
                                .param("size", "3")
                                .param("cursorId", 리뷰4.getId().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data.totalReviewNum").value("2"))
                .andExpect(jsonPath("$.result.data.reviews.last").value(true))
                .andExpect(jsonPath("$.result.data.reviews.content[0].reviewId").value(리뷰3.getId()))
                .andExpect(jsonPath("$.result.data.reviews.content[1].reviewId").value(리뷰2.getId()));
    }
}

