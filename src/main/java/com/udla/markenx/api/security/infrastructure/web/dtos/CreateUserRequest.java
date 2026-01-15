package com.udla.markenx.api.security.infrastructure.web.dtos;

import java.util.List;

public record CreateUserRequest(
    String username,
    String email,
    boolean enabled,
    boolean emailVerified,
    List<String> requiredActions
) {}
