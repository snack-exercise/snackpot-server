package com.soma.auth.controller;

import com.soma.auth.dto.request.SignInRequest;
import com.soma.auth.dto.request.SignUpRequest;
import com.soma.auth.service.AuthService;
import com.soma.util.response.Response;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Timed("SignUpTimer")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public Response signup(@RequestBody @Valid SignUpRequest request) {
        return Response.success(authService.signup(request));
    }

    @Timed("SignInTimer")
    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public Response login(@RequestBody @Valid SignInRequest request) {
        return Response.success(authService.login(request));
    }
}
