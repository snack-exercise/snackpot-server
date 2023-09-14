package com.soma.exercise.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.bodypart.entity.BodyPartType;
import com.soma.exercise.dto.request.ExerciseReadCondition;
import com.soma.exercise.dto.response.ExerciseResponse;
import com.soma.exercise.entity.Level;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.soma.exercise.entity.QExercise.exercise;
import static com.soma.exercise_bodypart.entity.QExerciseBodyPart.exerciseBodyPart;
import static com.soma.exercse_like.entity.QExerciseLike.exerciseLike;
import static com.soma.youtuber.entity.QYoutuber.youtuber;

@RequiredArgsConstructor
@Repository
public class ExerciseRepositoryImpl implements ExerciseRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Slice<ExerciseResponse> findAllByCondition(ExerciseReadCondition exerciseReadCondition, int size, String email) {
        JPAQuery<ExerciseResponse> query = jpaQueryFactory
                .select(Projections.fields(ExerciseResponse.class,
                        exercise.id.as("exerciseId"),
                        exercise.thumbnail,
                        exercise.title,
                        youtuber.name.as("youtuberName"),
                        exercise.timeSpent,
//                        JPAExpressions
//                                .select(bodyPart.bodyPartType)
//                                .from(bodyPart)
//                                .innerJoin(exerciseBodyPart).on(bodyPart.id.eq(exerciseBodyPart.bodyPart.id))
//                                .where(exerciseBodyPart.exercise.id.eq(exercise.id)),
                        JPAExpressions
                                .select(exerciseBodyPart.bodyPart.bodyPartType)
                                .from(exerciseBodyPart)
                                .innerJoin(exercise).on(exerciseBodyPart.exercise.id.eq(exercise.id)),
                        exercise.calories,
                        exercise.level
                ))
                .from(exercise)
                .leftJoin(youtuber).on(exercise.youtuber.id.eq(youtuber.id))
                .leftJoin(exerciseBodyPart).on(exercise.id.eq(exerciseBodyPart.exercise.id));

        if (email != null) {
            query.leftJoin(exerciseLike).on(exercise.id.eq(exerciseLike.exercise.id));
        }

        List<ExerciseResponse> results = query.where(eqBodyPartTypes(exerciseReadCondition.getBodyPartTypes()))
                .where(eqLevel(exerciseReadCondition.getLevel()))
                .where(eqLike(exerciseReadCondition.getLike(), email))
                .where(leTimeSpent(exerciseReadCondition.getTimeSpent()))
                .where(exercise.id.loe(exerciseReadCondition.getCursorId()))
                .orderBy(exercise.id.desc())
                .limit(size + 1)
                .fetch();

        boolean hasNext = results.size() > size;
        if (hasNext) {
            results.remove(results.size() - 1);
        }

        return new SliceImpl<>(results, PageRequest.of(0, size), hasNext);
    }

    private BooleanBuilder eqBodyPartTypes(List<BodyPartType> bodyPartTypes) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (bodyPartTypes != null) {
            for (BodyPartType bodyPartType : bodyPartTypes) {
                booleanBuilder.or(exerciseBodyPart.bodyPart.bodyPartType.eq(bodyPartType));
            }
        }

        return booleanBuilder.hasValue() ? booleanBuilder : null;
    }

    private BooleanExpression eqLevel(Level level) {
        if (level != null) {
            return exercise.level.eq(level);
        }
        return null;
    }

    private BooleanExpression eqLike(Boolean like, String email) {
        if (like != null) {
            return exerciseLike.member.email.eq(email);
        }
        return null;
    }

    private BooleanExpression leTimeSpent(Integer timeSpent) {
        if (timeSpent != null) {
            return exercise.timeSpent.loe(timeSpent);
        }
        return null;
    }
}
