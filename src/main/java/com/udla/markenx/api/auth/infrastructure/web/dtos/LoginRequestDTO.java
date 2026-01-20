package com.udla.markenx.api.auth.infrastructure.web.dtos;

public record LoginRequestDTO(
    String email,
    String password
) {}