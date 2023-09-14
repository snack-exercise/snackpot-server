package com.soma.group.controller;

import com.soma.group.dto.request.GroupCreateRequest;
import com.soma.group.service.GroupService;
import com.soma.util.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Exgroup", description = "운동 그룹 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Response create(@RequestBody GroupCreateRequest request, @AuthenticationPrincipal UserDetails loginUser){
        return Response.success(groupService.create(request, loginUser.getUsername()));
    }

}



