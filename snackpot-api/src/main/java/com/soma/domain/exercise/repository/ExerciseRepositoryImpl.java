package com.soma.domain.exercise.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.domain.bodypart.entity.BodyPartType;
import com.soma.domain.exercise.dto.request.ExerciseReadCondition;
import com.soma.domain.exercise.dto.response.ExerciseResponse;
import com.soma.domain.exercise.entity.Level;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.soma.domain.exercise.entity.QExercise.exercise;
import static com.soma.domain.exercise_bodypart.entity.QExerciseBodyPart.exerciseBodyPart;
import static com.soma.domain.exercise_like.entity.QExerciseLike.exerciseLike;
import static com.soma.domain.youtuber.entity.QYoutuber.youtuber;

@RequiredArgsConstructor
@Repository
public class ExerciseRepositoryImpl implements ExerciseRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Slice<ExerciseResponse> findAllByCondition(ExerciseReadCondition exerciseReadCondition, int size, String email) {

        BooleanExpression isLikedExpression = email != null ?
                JPAExpressions
                        .selectOne()
                        .from(exerciseLike)
                        .where(exerciseLike.exercise.id.eq(exercise.id)
                                .and(exerciseLike.member.email.eq(email)))
                        .exists() :
                JPAExpressions.selectZero().eq(1);

        JPAQuery<ExerciseResponse> query = jpaQueryFactory
                .select(Projections.constructor(ExerciseResponse.class,
                        exercise.id.as("exerciseId"),
                        exercise.thumbnail,
                        exercise.title,
                        youtuber.name.as("youtuberName"),
                        exercise.timeSpent,
                        exercise.calories,
                        exercise.level,
                        isLikedExpression
                ))
                .from(exercise)
                .leftJoin(youtuber).on(exercise.youtuber.id.eq(youtuber.id))
                .leftJoin(exerciseBodyPart).on(exercise.id.eq(exerciseBodyPart.exercise.id));

        List<ExerciseResponse> results = query.where(eqBodyPartTypes(exerciseReadCondition.getBodyPartTypes()))
                .where(eqLevel(exerciseReadCondition.getLevel()))
                .where(eqLike(exerciseReadCondition.getLike(), email))
                .where(leTimeSpent(exerciseReadCondition.getTimeSpent()))
                .where(loeCursorId(exerciseReadCondition.getCursorId()))
                .orderBy(exercise.id.desc())
                .limit(size + 1)
                .fetch();

        for (ExerciseResponse er : results) {
            List<BodyPartType> bodyPartTypes = jpaQueryFactory
                    .select(exerciseBodyPart.bodyPart.bodyPartType)
                    .from(exerciseBodyPart)
                    .where(exerciseBodyPart.exercise.id.eq(er.getExerciseId())).fetch();

            er.updateBodyPartTypes(bodyPartTypes);
        }

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

    private BooleanExpression loeCursorId(Long cursorId) {
        if (cursorId != null) {
            return exercise.id.loe(cursorId);
        }
        return null;
    }
}
