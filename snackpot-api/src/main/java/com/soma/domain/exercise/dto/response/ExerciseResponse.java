package com.soma.domain.exercise.dto.response;

import com.soma.domain.bodypart.entity.BodyPartType;
import com.soma.domain.exercise.entity.ExerciseType;
import com.soma.domain.exercise.entity.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExerciseResponse {
    private Long exerciseId;
    private String thumbnail;
    private String title;
    private String youtuberName;
    private Integer timeSpent;
    private List<BodyPartType> bodyPartTypes = new ArrayList<>();
    private Level level;
    private Integer calories;
    private Boolean isLiked;
    private ExerciseType exerciseType;

    public ExerciseResponse(Long exerciseId, String thumbnail, String title, String youtuberName, Integer timeSpent, Integer calories, Level level, Boolean isLiked, ExerciseType exerciseType) {
        this.exerciseId = exerciseId;
        this.thumbnail = thumbnail;
        this.title = title;
        this.youtuberName = youtuberName;
        this.timeSpent = timeSpent;
        this.calories = calories;
        this.level = level;
        this.isLiked = isLiked;
        this.exerciseType = exerciseType;
    }

    public void updateBodyPartTypes(List<BodyPartType> bodyPartTypes) {
        this.bodyPartTypes = bodyPartTypes;
    }
}
