package com.soma.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String name;

    private Integer dailyGoalTime;

    @NotBlank(message = "fcm 토큰은 필수 값입니다.")
    private String fcmToken;
}
