package com.soma.domain.exercise.dto.response;

import com.soma.domain.bodypart.entity.BodyPartType;
import com.soma.domain.exercise.entity.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private List<BodyPartType> bodyPartType;
    private Level level;
    private Integer calories;
    private Boolean isLiked;
}
