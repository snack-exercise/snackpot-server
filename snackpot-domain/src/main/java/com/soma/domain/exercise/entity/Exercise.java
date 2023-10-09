package com.soma.domain.exercise.entity;

import com.soma.common.BaseEntity;
import com.soma.domain.youtuber.entity.Youtuber;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Exercise extends BaseEntity {
    // todo : 시퀀스 생각해보기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Youtuber youtuber;

    private String videoId;

    private Integer calories;

    @Enumerated(EnumType.STRING)
    private Level level;

    private String effect;

    private String title;

    private String thumbnail;

    private Integer timeSpent;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    @Builder
    public Exercise(Youtuber youtuber, String videoId, Integer calories, Level level, String effect, String title, String thumbnail, Integer timeSpent, ExerciseType exerciseType) {
        this.youtuber = youtuber;
        this.videoId = videoId;
        this.calories = calories;
        this.level = level;
        this.effect = effect;
        this.title = title;
        this.thumbnail = thumbnail;
        this.timeSpent = timeSpent;
        this.exerciseType = exerciseType;
        active();
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void updateTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }
}
