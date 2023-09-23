package com.soma.domain.exercise.dto.response;

import com.soma.domain.bodypart.entity.BodyPart;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.entity.Level;
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
    private Integer time;
    private List<String> bodyPartNames;
    private Level level;
    private Integer calories;
    private Boolean like;

    public static ExerciseDetailResponse toDto(Exercise exercise, List<BodyPart> bodyPartList, Boolean isLike) {
        List<String> bodyPartNames = bodyPartList.stream().map(bodyPart -> bodyPart.getBodyPartType().name()).collect(Collectors.toList());

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
                isLike
        );
    }
}
