package com.soma.auth.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 1, max = 6, message = "이름은 1자 이상 6자 이하로 입력해주세요.")
    private String name;

    @NotNull(message = "일일 목표 시간은 필수입니다.")
    @Min(value = 0, message = "일일 목표 시간은 0초 이상이어야 합니다.")
    private Integer dailyGoalTime;

    @NotBlank(message = "fcm 토큰은 필수 값입니다.")
    private String fcmToken;
}
