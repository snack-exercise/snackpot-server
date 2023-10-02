package com.soma.domain.notification.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationCreateRequest {
    private Long groupId;
    private Long toUserId;
}
