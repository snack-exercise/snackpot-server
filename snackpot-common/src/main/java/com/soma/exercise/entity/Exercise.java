package com.soma.exercise.entity;

import com.soma.youtuber.entity.Youtuber;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Exercise {
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

    @Builder
    public Exercise(Youtuber youtuber, String videoId, Integer calories, Level level, String effect, String title, String thumbnail, Integer timeSpent) {
        this.youtuber = youtuber;
        this.videoId = videoId;
        this.calories = calories;
        this.level = level;
        this.effect = effect;
        this.title = title;
        this.thumbnail = thumbnail;
        this.timeSpent = timeSpent;
    }
}
