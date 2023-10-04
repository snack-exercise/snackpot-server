package com.soma.domain.exercise_like.repository;

import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise_like.entity.ExerciseLike;
import com.soma.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseLikeRepository extends JpaRepository<ExerciseLike, Long> {
    Boolean existsByMemberAndExercise(Member member, Exercise exercise);

    void deleteByMemberAndExercise(Member member, Exercise exercise);
}
