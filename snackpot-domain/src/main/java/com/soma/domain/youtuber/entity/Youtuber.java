package com.soma.domain.youtuber.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Youtuber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String channelId;

    private String description;

    private String name;

    private String profileImg;

    @Builder
    public Youtuber(String channelId, String description, String name, String profileImg) {
        this.channelId = channelId;
        this.description = description;
        this.name = name;
        this.profileImg = profileImg;
    }
}
