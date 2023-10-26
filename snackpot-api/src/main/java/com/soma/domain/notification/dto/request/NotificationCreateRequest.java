package com.soma.domain.notification.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationCreateRequest {
    private Long groupId;
    private Long toUserId;

    @NotBlank(message = "fcm 토큰은 필수 값입니다.")
    private String fcmToken;
}
