package com.soma.exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {
    EASY("쉬움"),
    MEDIUM("보통"),
    HARD("어려움");

    private final String levelType;
}
