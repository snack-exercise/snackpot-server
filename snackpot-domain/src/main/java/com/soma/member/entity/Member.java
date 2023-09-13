package com.soma.member.entity;

import com.soma.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private Integer dailyGoalTime;

    private String profileImg;

    private String fcmToken;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Builder
    public Member(String name, String email, Integer dailyGoalTime, String profileImg, String fcmToken, String password, RoleType roleType) {
        this.name = name;
        this.email = email;
        this.dailyGoalTime = dailyGoalTime;
        this.profileImg = profileImg;
        this.fcmToken = fcmToken;
        this.password = password;
        this.roleType = roleType;
        active();
    }

    public void updateFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

    public void deleteFcmToken(){
        this.fcmToken = null;
    }
}
