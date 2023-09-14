package com.soma.exercise.dto.request;

import com.soma.bodypart.entity.BodyPartType;
import com.soma.exercise.entity.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseReadCondition {
    private Long cursorId;
    private List<BodyPartType> bodyPartTypes;
    private Boolean like;
    private Level level;
    private Integer timeSpent;
}
