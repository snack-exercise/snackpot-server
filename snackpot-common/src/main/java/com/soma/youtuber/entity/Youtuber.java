package com.soma.youtuber.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Youtuber {
    @Id
    private Long id;

    private String channelId;

    private String description;

    private String name;

    private String profileImg;
}
