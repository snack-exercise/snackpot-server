package com.soma.exercise_bodypart.entity;

import com.soma.bodypart.entity.BodyPart;
import com.soma.common.BaseEntity;
import com.soma.exercise.entity.Exercise;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ExerciseBodyPart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exerciseId")
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bodyPartId")
    private BodyPart bodyPart;

    @Builder
    public ExerciseBodyPart(Exercise exercise, BodyPart bodyPart) {
        this.exercise = exercise;
        this.bodyPart = bodyPart;
    }
}
