package com.soma.domain.exercise.service;

import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.exception.exercise.ExerciseNotFoundException;
import com.soma.exception.member.MemberNotFoundException;
import com.soma.domain.exercise.dto.request.ExerciseFinishRequest;
import com.soma.domain.exercise.dto.request.ExerciseReadCondition;
import com.soma.domain.exercise.dto.response.ExerciseResponse;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise_record.repository.ExerciseRecordRepository;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.soma.common.constant.Status.ACTIVE;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseRecordRepository exerciseRecordRepository;
    private final MemberRepository memberRepository;

    public Slice<ExerciseResponse> readAllByCondition(ExerciseReadCondition condition, int size, String email) {
        return exerciseRepository.findAllByCondition(condition, size, email);
    }

    @Transactional
    public void finish(ExerciseFinishRequest request, String email) {
        Member member = memberRepository.findByEmailAndStatus(email, ACTIVE).orElseThrow(MemberNotFoundException::new);
        Exercise exercise = exerciseRepository.findByIdAndStatus(request.getExerciseId(), ACTIVE).orElseThrow(ExerciseNotFoundException::new);
        exerciseRecordRepository.save(ExerciseFinishRequest.toEntity(exercise, member));
    }
}
