package com.honorarios.honorarios_api.controller;

import com.honorarios.honorarios_api.dto.AuthResponse;
import com.honorarios.honorarios_api.dto.LoginRequest;
import com.honorarios.honorarios_api.dto.RegistrarRequest;
import com.honorarios.honorarios_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registrar(@Valid @RequestBody RegistrarRequest req) {
        return auth.registrar(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return auth.login(req);
    }
}
