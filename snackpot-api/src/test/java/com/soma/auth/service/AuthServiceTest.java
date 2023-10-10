package com.soma.auth.service;

import com.soma.auth.dto.request.SignUpRequest;
import com.soma.domain.group.dto.request.GroupCreateRequest;
import com.soma.domain.group.dto.response.GroupNameResponse;
import com.soma.domain.group.factory.dto.GroupCreateFactory;
import com.soma.domain.group.repository.GroupRepository;
import com.soma.domain.group.service.GroupService;
import com.soma.domain.joinlist.repository.JoinListRepository;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.factory.entity.MemberFactory;
import com.soma.domain.member.factory.fixtures.MemberFixtures;
import com.soma.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@SpringBootTest
//@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 비즈니스 로직 테스트")
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("같은 닉네임으로 동시에 회원가입을 시도할 때 Member 엔티티의 name 컬럼의 Unique 제약조건 덕분에 1명만 제대로 가입된다.")
    @Test
    void signupTest() throws Exception {
        int threadCount = 32;

        ExecutorService executorService = Executors.newFixedThreadPool(32);//32개 스레드 생성
        CountDownLatch latch = new CountDownLatch(threadCount); //스레드 완료 대기를 위해

        for(int i=0; i<threadCount; i++){
            executorService.submit(()->{
                try{
                    authService.signup(new SignUpRequest("테스트", 10, "fcmToken")); //문제의 메서드 호출
                } finally {
                    latch.countDown(); //완료되었음을 알림
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 기다렸다가, 모든 스레드가 완료되면 다음 코드로 진행
        long count = memberRepository.count();

        assertEquals(1L, count);
    }


    @Test
    @DisplayName("Member 엔티티의 name 컬럼에 unique 제약조건 설정을 확인한다.")
    void MemberNameUniqueConstraintTest() throws Exception {
        //given

        Member 김누구1 = MemberFactory.createUserRoleMemberWithNameAndEmail("김누구", "kim@gamilcom");
        Member 김누구2 = MemberFactory.createUserRoleMemberWithNameAndEmail("김누구", "kim@gamilcom");

        memberRepository.saveAndFlush(김누구1);


        //when //then
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(김누구2));
    }

}