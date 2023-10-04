package com.soma.domain.member.controller;

import com.soma.common.constant.Status;
import com.soma.domain.member.factory.entity.MemberFactory;
import com.soma.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        memberRepository.save(MemberFactory.createUserRoleMember());
        memberRepository.save(MemberFactory.createUserRoleMember());

        // when, then
        Long totalNum = memberRepository.countByStatus(Status.ACTIVE);
        mockMvc.perform(
                        get("/members/total-num")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.result.data.totalNum").value(totalNum));

    }
}