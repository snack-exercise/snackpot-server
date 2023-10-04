package com.soma.domain.notification.controller;

import com.soma.domain.notification.dto.request.NotificationCreateRequest;
import com.soma.domain.notification.service.NotificationService;
import com.soma.util.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Notification", description = "알림 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "콕 찌르기", description = "한 명의 회원에게 콕 찌르기 알림을 전송합니다.", security = { @SecurityRequirement(name = "Authorization") })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping()
    public Response create(@RequestBody NotificationCreateRequest request, @AuthenticationPrincipal UserDetails loginUser){
        notificationService.create(request, loginUser.getUsername());
        return Response.success();
    }
}
