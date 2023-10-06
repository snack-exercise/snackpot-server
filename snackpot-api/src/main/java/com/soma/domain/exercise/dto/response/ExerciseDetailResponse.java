package com.soma.domain.exercise.dto.response;

import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.entity.Level;
import com.soma.domain.exercise_bodypart.entity.ExerciseBodyPart;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class ExerciseDetailResponse {
    private String thumbnail;
    private String youtuberProfileImg;
    private String effect;
    private String videoId;
    private String title;
    private String youtuberName;
    private String youtuberChannelId;
    private String youtuberDescription;
    private Integer timeSpent;
    private List<String> bodyPartTypes;
    private Level level;
    private Integer calories;
    private Boolean isLiked;

    public static ExerciseDetailResponse toDto(Exercise exercise, List<ExerciseBodyPart> exerciseBodyPartList, Boolean isLiked) {
        List<String> bodyPartNames = exerciseBodyPartList.stream().map(exerciseBodyPart -> exerciseBodyPart.getBodyPart().getBodyPartType().name()).collect(Collectors.toList());

        return new ExerciseDetailResponse(
                exercise.getThumbnail(),
                exercise.getYoutuber().getProfileImg(),
                exercise.getEffect(),
                exercise.getVideoId(),
                exercise.getTitle(),
                exercise.getYoutuber().getName(),
                exercise.getYoutuber().getChannelId(),
                exercise.getYoutuber().getDescription(),
                exercise.getTimeSpent(),
                bodyPartNames,
                exercise.getLevel(),
                exercise.getCalories(),
                isLiked
        );
    }
}
