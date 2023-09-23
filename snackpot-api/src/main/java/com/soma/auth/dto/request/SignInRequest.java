package com.soma.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignInRequest {
    private String name;

    @NotBlank(message = "fcm 토큰은 필수 값입니다.")
    private String fcmToken;
}