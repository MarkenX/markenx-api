package com.udla.markenx.api.auth.infrastructure.web;

import com.udla.markenx.api.auth.infrastructure.keycloak.AuthService;
import com.udla.markenx.api.auth.infrastructure.web.dtos.LoginRequestDTO;
import com.udla.markenx.api.auth.infrastructure.web.dtos.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponseDTO>> login(@RequestBody LoginRequestDTO loginRequest) {
        return authService.login(loginRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(401).build());
    }
}