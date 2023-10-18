package com.soma.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignInRequest {
    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 1, max = 6, message = "이름은 1자 이상 6자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message = "fcm 토큰은 필수 값입니다.")
    private String fcmToken;
}