package com.soma.domain.youtuber.factory.entity;

import com.soma.domain.youtuber.entity.Youtuber;

import static com.soma.domain.youtuber.factory.fixtures.YoutuberFixtures.*;

public class YoutuberFactory {
    public static Youtuber createYoutuber() {
        return Youtuber.builder()
                .channelId(채널id)
                .description(설명)
                .name(이름)
                .profileImg(프로필사진).build();
    }
}
