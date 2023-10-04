package com.soma.domain.youtuber.entity;

import com.soma.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Youtuber  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String channelId;

    @Column(length = 1000)
    private String description;

    private String name;

    private String profileImg;

    @Builder
    public Youtuber(String channelId, String description, String name, String profileImg) {
        this.channelId = channelId;
        this.description = description;
        this.name = name;
        this.profileImg = profileImg;
        active();
    }

    public void updateProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}
