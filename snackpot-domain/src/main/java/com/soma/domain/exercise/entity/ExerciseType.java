package com.soma.domain.exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExerciseType {
    CARDIO("유산소"),
    WEIGHT("근력"),
    REHABILITATION("재활"),
    YOGA("요가");

    private final String name;
}
