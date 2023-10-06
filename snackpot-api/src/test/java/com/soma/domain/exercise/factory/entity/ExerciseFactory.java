package com.soma.domain.exercise.factory.entity;

import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.youtuber.entity.Youtuber;

import static com.soma.domain.exercise.factory.fixtures.ExerciseFixtures.*;

public class ExerciseFactory {
    public static Exercise createExerciseWithYoutuber(Youtuber youtuber) {
        return Exercise.builder()
                .youtuber(youtuber)
                .videoId(영상id)
                .calories(칼로리)
                .level(난이도)
                .effect(효과)
                .title(제목)
                .thumbnail(썸네일)
                .timeSpent(영상길이)
                .build();
    }

    public static Exercise createExerciseWithYoutuberAndVideoId(Youtuber youtuber, String videoId) {
        return Exercise.builder()
                .youtuber(youtuber)
                .videoId(videoId)
                .calories(칼로리)
                .level(난이도)
                .effect(효과)
                .title(제목)
                .thumbnail(썸네일)
                .timeSpent(영상길이)
                .build();
    }
}
