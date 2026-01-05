package com.udla.markenx.api.classroom.users.infrastructure.keycloak.dtos;

import java.util.List;

public record CreateUserRequest(
    String username,
    String email,
    boolean enabled,
    boolean emailVerified,
    List<String> requiredActions
) {}
