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
    public Member(String email, String name, Integer dailyGoalTime) {
        this.email = email;
        this.name = name;
        this.dailyGoalTime = dailyGoalTime;
    }

    public void updateFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

    public void deleteFcmToken(){
        this.fcmToken = null;
    }
}
