package com.soma.domain.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationMessage {
    REMINDER1("님의 콕 찌르기", "같이 운동해요! 💪"),
    REMINDER2("님의 콕 찌르기", "아자아자 화이팅! 🔥"),
    REMINDER3("님의 콕 찌르기", "작심삼일 아니되오! 😄"),
    REMINDER4("님의 콕 찌르기", "지금 당장 운동 시작! 👍🏻");

    private final String title;
    private final String body;
    public String getTitleWithNicknameAndGroupName(String groupName, String nickname) {
        return "[" + groupName + "] " + nickname + title;
    }
}
