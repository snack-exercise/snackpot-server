package com.soma.domain.exercise.service;

import com.soma.common.constant.Status;
import com.soma.domain.exercise.dto.request.ExerciseFinishRequest;
import com.soma.domain.exercise.factory.fixtures.ExerciseFixtures;
import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.domain.exercise_record.entity.ExerciseRecord;
import com.soma.domain.exercise_record.repository.ExerciseRecordRepository;
import com.soma.domain.member.factory.fixtures.MemberFixtures;
import com.soma.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.soma.domain.exercise.factory.dto.ExerciseFinishFactory.createExerciseFinishRequest;
import static com.soma.domain.exercise.factory.entity.ExerciseFactory.createExerciseWithYoutuber;
import static com.soma.domain.member.factory.entity.MemberFactory.createUserRoleMember;
import static com.soma.domain.youtuber.factory.entity.YoutuberFactory.createYoutuber;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExerciseService 비즈니스 로직 테스트")
class ExerciseServiceTest {
    @InjectMocks
    private ExerciseService exerciseService;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private ExerciseRecordRepository exerciseRecordRepository;
    @Mock
    private MemberRepository memberRepository;

    @DisplayName("운동을 완료한다.")
    @Test
    void finish() throws Exception {
        // given
        ExerciseFinishRequest request = createExerciseFinishRequest(ExerciseFixtures.운동id);
        given(memberRepository.findByEmailAndStatus(anyString(), any(Status.class))).willReturn(Optional.of(createUserRoleMember()));
        given(exerciseRepository.findByIdAndStatus(anyLong(), any(Status.class))).willReturn(Optional.of(createExerciseWithYoutuber(createYoutuber())));

        // when
        exerciseService.finish(request, MemberFixtures.이메일);

        // then
        verify(exerciseRecordRepository, times(1)).save(any(ExerciseRecord.class));
    }
}