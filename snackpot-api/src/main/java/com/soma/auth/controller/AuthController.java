package com.soma.auth.controller;

import com.soma.auth.dto.request.SignInRequest;
import com.soma.auth.dto.request.SignUpRequest;
import com.soma.auth.service.AuthService;
import com.soma.util.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public Response signup(@RequestBody @Valid SignUpRequest request) {
        return Response.success(authService.signup(request));
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public Response login(@RequestBody @Valid SignInRequest request) {
        return Response.success(authService.login(request));
    }
}
