package br.edu.infnet.continuum_control.auth.api.controller;

import br.edu.infnet.continuum_control.auth.api.dto.LoginRequest;
import br.edu.infnet.continuum_control.auth.api.dto.LoginResponse;
import br.edu.infnet.continuum_control.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }
}