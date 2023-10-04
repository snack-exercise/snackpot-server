package com.soma.domain.exercise_like.service;

import com.soma.common.constant.Status;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.domain.exercise_like.entity.ExerciseLike;
import com.soma.domain.exercise_like.repository.ExerciseLikeRepository;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.exception.exercise.ExerciseNotFoundException;
import com.soma.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExerciseLikeService {
    private final ExerciseLikeRepository exerciseLikeRepository;
    private final MemberRepository memberRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public void create(Long exerciseId, String email) {
        Exercise exercise = exerciseRepository.findByIdAndStatus(exerciseId, Status.ACTIVE).orElseThrow(ExerciseNotFoundException::new);
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(MemberNotFoundException::new);

        if (!exerciseLikeRepository.existsByMemberAndExercise(member, exercise)) {
            ExerciseLike exerciseLike = ExerciseLike.builder().exercise(exercise).member(member).build();
            exerciseLikeRepository.save(exerciseLike);
        }
    }

    @Transactional
    public void delete(Long exerciseId, String email) {
        Exercise exercise = exerciseRepository.findByIdAndStatus(exerciseId, Status.ACTIVE).orElseThrow(ExerciseNotFoundException::new);
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(MemberNotFoundException::new);

        exerciseLikeRepository.deleteByMemberAndExercise(member, exercise);
    }
}
