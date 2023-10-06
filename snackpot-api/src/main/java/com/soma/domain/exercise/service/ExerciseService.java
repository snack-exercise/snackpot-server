package com.soma.domain.exercise.service;

import com.soma.domain.bodypart.entity.BodyPart;
import com.soma.domain.bodypart.entity.BodyPartType;
import com.soma.domain.exercise.dto.request.ExerciseFinishRequest;
import com.soma.domain.exercise.dto.request.ExerciseReadCondition;
import com.soma.domain.exercise.dto.response.ExerciseDetailResponse;
import com.soma.domain.exercise.dto.response.ExerciseResponse;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.domain.exercise_bodypart.repository.ExerciseBodypartRepository;
import com.soma.domain.exercise_like.repository.ExerciseLikeRepository;
import com.soma.domain.exercise_record.repository.ExerciseRecordRepository;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.exception.exercise.ExerciseNotFoundException;
import com.soma.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.soma.common.constant.Status.ACTIVE;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseRecordRepository exerciseRecordRepository;
    private final MemberRepository memberRepository;
    private final ExerciseBodypartRepository exerciseBodypartRepository;

    private final ExerciseLikeRepository exerciseLikeRepository;

    public Slice<ExerciseResponse> readAllByCondition(ExerciseReadCondition condition, List<BodyPartType> bodyPartTypes, int size, String email) {
        return exerciseRepository.findAllByCondition(condition, bodyPartTypes, size, email);
    }

    @Transactional
    public void finish(ExerciseFinishRequest request, String email) {
        Member member = memberRepository.findByEmailAndStatus(email, ACTIVE).orElseThrow(MemberNotFoundException::new);
        Exercise exercise = exerciseRepository.findByIdAndStatus(request.getExerciseId(), ACTIVE).orElseThrow(ExerciseNotFoundException::new);
        exerciseRecordRepository.save(ExerciseFinishRequest.toEntity(exercise, member));
    }

    public ExerciseDetailResponse readExerciseDetails(Long exerciseId, String email) {
        Exercise exercise = exerciseRepository.findByIdAndStatus(exerciseId, ACTIVE).orElseThrow(ExerciseNotFoundException::new);
        List<BodyPart> bodyPartList = exerciseBodypartRepository.findAllByExerciseIdAndStatus(exerciseId, ACTIVE);
        Member member = memberRepository.findByEmailAndStatus(email, ACTIVE).orElseThrow(MemberNotFoundException::new);
        Boolean isLiked = exerciseLikeRepository.existsByMemberAndExercise(member, exercise);
        return ExerciseDetailResponse.toDto(exercise, bodyPartList, isLiked);
    }
}