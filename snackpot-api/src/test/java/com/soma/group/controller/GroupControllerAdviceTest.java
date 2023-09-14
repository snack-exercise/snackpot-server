package com.soma.group.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soma.advice.ExceptionAdvice;
import com.soma.exception.MemberNotFoundException;
import com.soma.group.dto.request.GroupCreateRequest;
import com.soma.group.factory.dto.GroupCreateFactory;
import com.soma.group.service.GroupService;
import com.soma.security.TestUserArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.soma.advice.ErrorCode.MEMBER_NOT_FOUND_EXCEPTION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GroupControllerAdviceTest {
    @InjectMocks
    private GroupController groupController;
    @Mock
    GroupService groupService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController)
                .setCustomArgumentResolvers(new TestUserArgumentResolver())
                .setControllerAdvice(new ExceptionAdvice())
//                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }


    @Test
    @DisplayName("운동 그룹 생성 시, MemberNotFoundException가 발생했을 때 적절한 오류 메시지를 반환한다.")
    void MemberNotFoundExceptionTest() throws Exception {
        //given
        GroupCreateRequest request = GroupCreateFactory.createGroupCreateRequest();
        when(groupService.create(any(), anyString())).thenThrow(MemberNotFoundException.class);

        //when //then
        mockMvc.perform(
                        post("/groups")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(MEMBER_NOT_FOUND_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.result.message").value(MEMBER_NOT_FOUND_EXCEPTION.getMessage()))
                .andDo(print());
    }

}
