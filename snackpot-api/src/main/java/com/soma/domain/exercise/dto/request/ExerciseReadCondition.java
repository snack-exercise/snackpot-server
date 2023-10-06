package com.soma.domain.exercise.dto.request;

import com.soma.domain.exercise.entity.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseReadCondition {
    private Long cursorId;
    private Boolean like;
    private Level level;
    private Integer timeSpent;
}
